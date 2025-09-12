package com.example.decathlon.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final Map<String, Map<String,Integer>> scores = new LinkedHashMap<>();

    @PostMapping("/competitors")
    public ResponseEntity<?> add(@RequestBody Map<String,String> body) {
        String name = Optional.ofNullable(body.get("name")).orElse("").trim();
        if (name.isEmpty()) return ResponseEntity.badRequest().body("Empty name");
        scores.putIfAbsent(name, new LinkedHashMap<>());
        return ResponseEntity.status(201).build();
    }

   @PostMapping("/score")
    public Map<String,Integer> score(@RequestBody ScoreReq r) {
        int pts = calculate(r.event(), r.raw());
        scores.computeIfAbsent(r.name().trim(), k -> new LinkedHashMap<>())
                .put(r.event(), pts);
        return Map.of("points", pts);
    }

   @GetMapping("/standings")
    public List<Map<String,Object>> standings() {
        List<Map<String,Object>> out = new ArrayList<>();
        for (var e : scores.entrySet()) {
            var row = new LinkedHashMap<String,Object>();
            row.put("name", e.getKey());
            row.put("scores", new LinkedHashMap<>(e.getValue()));
            int total = e.getValue().values().stream().mapToInt(i->i).sum();
            row.put("total", total);
            out.add(row);
        }
        // sortera fallande på total (samma som frontend förväntar sig)
        out.sort((a,b) -> Integer.compare((int)b.get("total"), (int)a.get("total")));
        return out;
    }

    @GetMapping(value="/export.csv", produces = MediaType.TEXT_PLAIN_VALUE)
    public String exportCsv() {
        // samla alla eventkolumner som förekommer
        Set<String> eventIds = new LinkedHashSet<>();
        scores.values().forEach(m -> eventIds.addAll(m.keySet()));

        StringBuilder sb = new StringBuilder();
        sb.append("Name");
        for (String ev : eventIds) sb.append(',').append(ev);
        sb.append(",Total\n");

        for (var e : scores.entrySet()) {
            String name = e.getKey();
            Map<String,Integer> pts = e.getValue();
            int sum = pts.values().stream().mapToInt(i->i).sum();

            sb.append(name); // OBS: inga citattecken → avsiktligt “naiv” CSV
            for (String ev : eventIds) {
                sb.append(',');
                Integer p = pts.get(ev);
                if (p != null) sb.append(p);
            }
            sb.append(',').append(sum).append('\n');
        }
        return sb.toString();
    }

    public record ScoreReq(String name, String event, double raw) {}

    private int calculate(String eventId, double raw) {
        switch (eventId) {
            case "100m":    return track(25.4347, 18.0, 1.81, raw);    // sekunder
            case "longJump":return field (0.14354, 220.0, 1.4,  raw);  // cm
            case "shotPut": return field (51.39,   1.5,  1.05, raw);   // meter
            case "400m":    return track(1.53775,  82.0, 1.81, raw);   // sekunder
            default: return 0;
        }
    }
    private int track(double A, double B, double C, double t) {
        double x = B - t; if (x <= 0) return 0;
        return (int)Math.floor(A * Math.pow(x, C));
    }
    private int field(double A, double B, double C, double d) {
        double x = d - B; if (x <= 0) return 0;
        return (int)Math.floor(A * Math.pow(x, C));
    }
}
