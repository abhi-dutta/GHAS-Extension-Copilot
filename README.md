# GHAS-Copilot-Extension


**Intent:**

Typically if you see, our Supply Chain Security features kicks in once the code has been checked into the GitHub repo and then Dependency Graph and Dependabot Security is enabled. At this point, the features are already implemented and we have gone way past the design phase.The intent of this extension is to shift further left and help devs and architects to decide whether or not a package that the team is intending to use has existing vulnerabilities. I have leveraged the GitHub models (gpt-40-mini to be precise) to extract the specific package and version info from the user input string and then subsequently calling the GitHub Security Advisory API's to extract up to date vulnerability info. 

**Design Approach:**

From a design approach perspective, I am using the concept of Service Chaining to tie up differetn service calls. Here you will find a core application written in Java (SpringBoot) where we have the actual logic of calling the OpeAI service and GitHub API's to extract the vulnerability information for a user input 3rd party package. Since the heavy lifting is done by the individual microservices (in this case the dependabot vulnerability details extractor), in the future, if we want to add more capabilities areound GHAS to the Copilot Extension, one can write business logic in the technology of their choice, and hook on to the Node interceptor module. 


![image](/artifacts/Achitecture-Blueprint.png)


**Implemenation Details:**
You will see an node implementation under `/src/express/extension.js`. You can think of this service as a dummy interceptor of the messages from Copilot Chat. The Chat instance would send the user input to the node application. The Node application in turn would call the Spring Boot microservice passing the user prompt from the user. Since, this application is written specifically to extract latest vulnerability information for a given package name and version, the user prompt would be sent to a OpenAI API endpoint to extract only the package name and version from the user input. We request the OpenAI model (gpt-4o-mini) to respond back with the package name and version in a json format

Once the JSON data with the package name and version is received in the SpringBoot application, we call an api endpoint on the GitHub Advisory Database to see if any vulnerabilitiy exists for the named package.

If a vulnerability is found in the package, the microservice will construct a markdown table with several vulnerability data extracted. The specific data extracted and responded back and rendered is as below 

`| GHSAID | CVEID | URL | SUMMARY | SEVERITY | ECOSYSTEM | NAME | VULNERABLE VERSION RANGE | PATCHED VERSION |`

The microservice then responds back to the node interceptor with the markdown table as a String object. The Node interceptor will in turn respond back to the GitHub Coopilot Chat UI / VS Code Copilot Chat prompt with the intended response.

Below is a sequence diagram showing the execution flow


![image](/artifacts/SequenceFlow.png)


**The Request to & Response from the Copilot Extension:**




![image](/artifacts/Prompt.png)




**Demo Recording:**

[![GHAS Copilot Extension](https://github.com/user-attachments/assets/ff583e4c-5432-47a8-b7e0-89d477ed9efb)](https://www.youtube.com/watch?v=UUtx6DzuTtg)





