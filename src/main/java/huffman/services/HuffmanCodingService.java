package huffman.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.PriorityQueue;

@Service
public class HuffmanCodingService {

    public HashMap<Character, String> buildHuffmanTree(String message) {
        String cleanedMessage = message.trim().replaceAll("[^\\p{L}\\p{N}]", "");

        HashMap<Character, Integer> frequencyMap = new HashMap<>();

        for (char c : cleanedMessage.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a.frequency == b.frequency) {
                return Character.compare(a.data, b.data);
            }
            return a.frequency - b.frequency;
        });

        for (char c : frequencyMap.keySet()) {
            priorityQueue.add(new HuffmanNode(c, frequencyMap.get(c)));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode newNode = new HuffmanNode('$', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.add(newNode);
        }

        HuffmanNode root = priorityQueue.poll();
        HashMap<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);

        System.out.println("Frequency Map: " + frequencyMap);
        System.out.println("Huffman Codes: " + huffmanCodes);
        return huffmanCodes;
    }

    private void generateCodes(HuffmanNode root, String code, HashMap<Character, String> huffmanCodes) {
        if (root == null) return;

        if (root.data != '$') {
            huffmanCodes.put(root.data, code);
        }

        generateCodes(root.left, code + '0', huffmanCodes);
        generateCodes(root.right, code + '1', huffmanCodes);
    }

    public String encode(String message) {
        HashMap<Character, String> huffmanCodes = buildHuffmanTree(message);
        StringBuilder encodedString = new StringBuilder();

        for (char c : message.toCharArray()) {
            String code = huffmanCodes.get(c);
            if (code != null) {
                encodedString.append(code);
            } else {
                System.out.println("Code not found for character: " + c);
            }
        }

        return encodedString.toString();
    }

    public HashMap<Character, Double> calculateProbabilities(String message) {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        int totalLength = 0;

        for (char c : message.toCharArray()) {
            if (c != '"') {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
                totalLength++;
            }
        }

        HashMap<Character, Double> probabilities = new HashMap<>();
        for (char c : frequencyMap.keySet()) {
            probabilities.put(c, (double) frequencyMap.get(c) / totalLength);
        }

        return probabilities;
    }

    public String getCodeForCharacter(String message, char character) {
        HashMap<Character, String> huffmanCodes = buildHuffmanTree(message);
        return huffmanCodes.getOrDefault(character, "Character not found in the message");
    }
}