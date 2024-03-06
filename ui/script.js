function resetResultContainer() {
    var resultContainer = document.getElementById('resultContainer');
    var resultElement = document.getElementById('result');
    var downloadButton = document.getElementById('downloadButton');
    var loadingIndicator = document.getElementById('loadingIndicator');

    // Hide the result container, download button, and loading indicator
    resultContainer.style.display = 'none';
    downloadButton.style.display = 'none';
    loadingIndicator.style.display = 'none';

    // Clear any previous content in the result element
    resultElement.innerHTML = '';
}

document.getElementById('textInput').addEventListener('input', resetResultContainer);


function submitText() {
    var userInput = document.getElementById('textInput').value;
    var resultContainer = document.getElementById('resultContainer');
    var resultElement = document.getElementById('result');

    loadingIndicator.style.display = 'block';

    if (userInput.trim() !== "") {
        var apiUrl = 'http://localhost:8088/openapi?message=' + encodeURIComponent(userInput);

        fetch(apiUrl)
            .then(response => response.text())
            .then(yamlData => {
                try {
                    var formattedYaml = jsyaml.dump(jsyaml.load(yamlData));
                    loadingIndicator.style.display = 'none';
                    resultElement.innerHTML = "<pre>" + formattedYaml + "</pre>";
                    resultContainer.style.display = 'block'; 
                    if (yamlData.trim().toLowerCase().startsWith('openapi')) {
                        downloadButton.style.display = 'block';
                    }

                }  catch (error) {
                    loadingIndicator.style.display = 'none';
                    console.error('Error parsing YAML data:', error);
                }
            })
            .catch(error => console.error('Error:', error));
    } else {
        resultContainer.style.display = 'none';
    }
}

async function downloadYaml() {
    var yamlContent = document.getElementById('result').textContent;
    try {
        const options = {
            types: [
                {
                    description: 'YAML Files',
                    accept: { 'application/yaml': ['.yaml', '.yml'] },
                },
            ],
        };

        const fileHandle = await window.showSaveFilePicker(options);
        const writable = await fileHandle.createWritable();

        await writable.write(yamlContent);
        await writable.close();
    } catch (error) {
        console.error('Error saving file:', error);
    }
}

// function downloadYaml() {
//     var yamlContent = document.getElementById('result').textContent;

//     var blob = new Blob([yamlContent], { type: 'application/yaml' });
//     var url = window.URL.createObjectURL(blob);
//     var a = document.createElement('a');
//     a.href = url;
//     a.download = 'downloaded_file.yaml';
//     document.body.appendChild(a);
//     a.click();
//     document.body.removeChild(a);
//     window.URL.revokeObjectURL(url);
// }

