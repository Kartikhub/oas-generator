package com.crent.controller;

import com.crent.service.AIService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
public class SimpleAiController {

	private String generatedResult;

	@Autowired
	private AIService aiService;
	@GetMapping("/openapi")
	public ResponseEntity<?> fetchOpenAPISpecification(@RequestParam(value = "message", defaultValue = "") String message) {
		try {
			generatedResult = aiService.getOpenAPISpec(message);
			return ResponseEntity.ok(generatedResult);
		} catch (IllegalArgumentException exception) {
			return ResponseEntity.status(400).body("Error: " + exception.getMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}


	@GetMapping("/download")
	public ResponseEntity<?> downloadResult() throws IOException {
		// Create a temporary file with the generated result
		if(generatedResult==null || generatedResult.isEmpty()) {
			return ResponseEntity.status(400).body("Error: No file to download");
		}
		Path tempFile = Files.createTempFile("generatedResult", ".yaml");
		Files.write(tempFile, generatedResult.getBytes(), StandardOpenOption.WRITE);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "result.yaml");

		byte[] fileContent = Files.readAllBytes(tempFile);

		Files.delete(tempFile);

		return ResponseEntity.ok()
				.headers(headers)
				.body(fileContent);
	}

}
