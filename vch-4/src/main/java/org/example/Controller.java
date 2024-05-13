package org.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.Main2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/app-controller")
@CrossOrigin(origins = "*")
public class Controller {
    Main2 main = new Main2();

    @PostMapping("/points")
    public ResponseEntity points(@RequestBody HashMap points) {
        main.remove();
        ArrayList<Object> ans = main.start(points);
        return ResponseEntity.ok(ans);
    }
    @GetMapping("/functions")
    public ResponseEntity functions() {
        ArrayList<ArrayList<Double>> ans = main.functions();
        return ResponseEntity.ok(ans);
    }

    @GetMapping("/welcome")
    public ResponseEntity welcome() {
        return ResponseEntity.ok("welcome");
    }
}
