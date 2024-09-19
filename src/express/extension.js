
const { createReadStream } = require('fs')
const express = require('express')
const axios = require("axios");
require('dotenv').config();

const serviceUrl = process.env.DEPENDENCY_SERVICE_URL;

console.log(`serviceUrl is: ${serviceUrl}`)


const app = express()
app.use(express.json())

app.get('/', (req, res) => {
  res.send('Hello from Copilot Extension')
})

app.post('/', async (req, res) => {
  // This endpoint receives the chat messages from the client 
  
  let messages = [];
  if (req.body && req.body.messages) {
    messages = req.body.messages
      .filter(({ content, name }) => content && !name)
      .map(({ role, content }) => ({
        role,
        content
      }));
  }

  let contentMessage = "";
  if (messages.length > 0) {
    contentMessage = messages[messages.length - 1].content;
  }

  console.log("messages", contentMessage);

   // Call the external REST API with the message as input
   const response = await axios.post(serviceUrl, {
    data: contentMessage,
  });

  const data = {
    "id": "chatcmpl-123",
    "object": "chat.completion.chunk",
    "created": (new Date()).getTime(),
    "model": "gpt-4-1106-preview",
    "system_fingerprint": "fp_44709d6fcb",
    "choices": [
      {
        "index": 0,
        "delta": {
          "content": response.data
        },
        "logprobs": null,
        "finish_reason": null
      }
    ]
  };

  // Set the appropriate headers
  res.setHeader('Content-Type', 'application/json');

  // Stream the JSON data back to the response
  res.write(`data: ${JSON.stringify(data)}\n\n`, (err) => {
    if (err) {
      console.error(`Error writing first chunk: ${err}`);
    }
    console.log('First chunk was written!');
  });
  res.end();

})

const PORT = process.env.PORT || 3000;
app.listen(PORT)
