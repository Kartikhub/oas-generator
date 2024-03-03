import * as jsyaml from 'js-yaml';

function submitText() {
    var userInput = document.getElementById('textInput').value;
    var resultContainer = document.getElementById('resultContainer');
    var resultElement = document.getElementById('result');

    // Check if userInput is not empty
    if (userInput.trim() !== "") {
        // resultElement.innerHTML = "Submitted Text: " + userInput;
        // resultContainer.style.display = 'block'; // Show the result container

        var apiUrl = 'http://localhost:8080/' + userInput;
        
        // Make a GET request to the REST endpoint using fetch

        var jsonResult = {
            "generation": "```yaml\nopenapi: 3.0.0\ninfo:\n  title: Car API\n  version: 1.0.0\npaths:\n  /car:\n    post:\n      summary: Add a new car\n      requestBody:\n        required: true\n        content:\n          application/json:\n            schema:\n              $ref: '#/components/schemas/Car'\n      responses:\n        '200':\n          description: Successful operation\n    get:\n      summary: Get car details by id\n      parameters:\n        - name: id\n          in: query\n          required: true\n          schema:\n            type: integer\n      responses:\n        '200':\n          description: Successful operation\n          content:\n            application/json:\n              schema:\n                $ref: '#/components/schemas/Car'\ncomponents:\n  schemas:\n    Car:\n      type: object\n      properties:\n        engine:\n          type: string\n        speed:\n          type: integer\n        id:\n          type: integer\n```"
          };

        var yamlObject = jsyaml.load(jsonResult.generation);
            // Convert the JavaScript object back to YAML
        var yamlContent = jsyaml.dump(yamlObject, { lineWidth: -1 });
        // Display the YAML content in the result container
        resultElement.innerHTML = "<pre>" + yamlContent + "</pre>";
        resultContainer.style.display = 'block';
        
          // Extract YAML content from the generation property
        //   var yamlContent = jsonResult.generation.match(/```yaml([\s\S]*?)```/)[1];
        //   resultElement.innerHTML = JSON.stringify(yamlContent);
        //   resultContainer.style.display = 'block';
        //   console.log(yamlContent);

        // fetch(apiUrl)
        //     .then(response => response.json())
        //     .then(data => {
        //         // Display the result in the result container
        //         resultElement.innerHTML = JSON.stringify(data);
        //         resultContainer.style.display = 'block'; // Show the result container
        //     })
        //     .catch(error => console.error('Error:', error));
    } else {
        // If userInput is empty, hide the result container
        resultContainer.style.display = 'none';
    }
}
