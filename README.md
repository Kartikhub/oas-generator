# Generate OpenAPI Specification using AI

This project contains a web service that will generate an OpenAPI Specification. 

## Building and running

```
./mvnw spring-boot:run
```

## Sample Prompt
Provide the below prompt in the UI -
```text
Create a component travel with attributes like id, origin, destination, departure date and origin date. Also provide paths for all the crud operations
```

## Access the endpoint

```bash
curl "http://localhost:8088/openapi?message=Schema%20-%20book%20and%20library%20Book%20attributes%20are%20like%20author%20name%20id%20and%20library%20attributes%20like%20id%20name%20section"
```

To Download the file hit the below nedpoint in your browser
```text
http://localhost:8088/download
```