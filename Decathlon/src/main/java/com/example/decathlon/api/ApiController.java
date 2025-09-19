package com.example.decathlon.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    // whitelist of valid event IDs
    private static final Set<String> EVENT_KEYS = Set.of(
            "100m","longJump","shotPut","highJump","400m",
            "110mHurdles","discus","poleVault","javelin","1500m"
    );

    private final Map<String, Map<String,Integer>> scores = new LinkedHashMap<>();

    @PostMapping("/competitors")
    public ResponseEntity<?> add(@RequestBody Map<String,String> body) {
        String name = Optional.ofNullable(body.get("name")).orElse("").trim();
        if (name.isEmpty()) return ResponseEntity.badRequest().body("Empty name");
        scores.putIfAbsent(name, new LinkedHashMap<>());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/score")
    public ResponseEntity<Map<String,Integer>> score(@RequestBody ScoreReq r) {
        System.out.println("DEBUG /score event=" + r.event() + " name=" + r.name() + " raw=" + r.raw());

        if (r.event() == null || !EVENT_KEYS.contains(r.event())) {
            return ResponseEntity.badRequest().body(Map.of("error", -1));
        }

        int pts = calculate(r.event(), r.raw());
        scores.computeIfAbsent(r.name().trim(), k -> new LinkedHashMap<>())
                .put(r.event(), pts);
        return ResponseEntity.ok(Map.of("points", pts));
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
        out.sort((a,b) -> Integer.compare((int)b.get("total"), (int)a.get("total")));
        return out;
    }

    @GetMapping(value="/export.csv", produces = MediaType.TEXT_PLAIN_VALUE)
    public String exportCsv() {
        // gather all events that appear
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

            sb.append(name);
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
        return switch (eventId) {
            case "100m" -> track(25.4347, 18.0, 1.81, raw);
            case "longJump" -> field(0.14354, 220.0, 1.4, raw);
            case "shotPut" -> field(51.39, 1.5, 1.05, raw);
            case "highJump" -> field(0.8465, 75.0, 1.42, raw);
            case "400m" -> track(1.53775, 82.0, 1.81, raw);
            case "110mHurdles" -> track(5.74352, 28.5, 1.92, raw);
            case "discus" -> field(12.91, 4.0, 1.1, raw);
            case "poleVault" -> field(0.2797, 100.0, 1.35, raw);
            case "javelin" -> field(10.14, 7.0, 1.08, raw);
            case "1500m" -> track(0.03768, 480.0, 1.85, raw);
            default -> 0;
        };
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
