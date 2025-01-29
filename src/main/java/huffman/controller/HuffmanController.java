package huffman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import huffman.services.HuffmanCodingService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/huffman")
public class HuffmanController {

    private final HuffmanCodingService huffmanCodingService;

    public HuffmanController(HuffmanCodingService huffmanCodingService) {
        this.huffmanCodingService = huffmanCodingService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String text) {
        if (text == null || text.isEmpty()) {
            return ResponseEntity.badRequest().body("Input text cannot be null or empty");
        }
        String encodedText = huffmanCodingService.encode(text);
        return ResponseEntity.ok(encodedText);
    }

    @PostMapping("/probabilities")
    public ResponseEntity<HashMap<Character, Double>> getProbabilities(@RequestBody String text) {
        if (text == null || text.isEmpty()) {
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
        HashMap<Character, Double> probabilities = huffmanCodingService.calculateProbabilities(text);
        return ResponseEntity.ok(probabilities);
    }

    @PostMapping("/code")
    public ResponseEntity<String> getCodeForCharacter(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String characterStr = request.get("character");

        if (text == null || text.isEmpty() || characterStr == null || characterStr.length() != 1) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        char character = characterStr.charAt(0);
        String code = huffmanCodingService.getCodeForCharacter(text, character);
        return ResponseEntity.ok(code);
    }
}