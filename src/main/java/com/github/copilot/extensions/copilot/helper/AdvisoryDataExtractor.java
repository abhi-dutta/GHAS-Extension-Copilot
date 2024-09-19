package com.github.copilot.extensions.copilot.helper;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdvisoryDataExtractor {

    
    public String getSecurityAdvisories(String jsonData) {

        String ecosystem = "";
        String name = "";
        String vulnerableVersionRange = "";
        String firstPatchedVersion = "";

        // Markdown table header
        StringBuilder markdownTable = new StringBuilder();
        markdownTable.append("| GHSAID | CVEID | URL | SUMMARY | SEVERITY | ECOSYSTEM | NAME | VULNERABLE VERSION RANGE | PATCHED VERSION |\n");
        markdownTable.append("| ------ | ----- | --- | ------- | -------- | --------- | ---- | ------------------------ | --------------- |\n");
        
        // Parse JSON array
        JSONArray advisories = new JSONArray(jsonData);

        if(advisories.length() == 0) {
            return "No advisories found";
        }

        // Loop through the JSON array
        for (int i = 0; i < advisories.length(); i++) {
            JSONObject advisory = advisories.getJSONObject(i);

            String ghsaId = advisory.getString("ghsa_id");
            String cveId = advisory.optString("cve_id", "N/A");
            String url = advisory.getString("html_url");
            String severity = advisory.getString("severity");
            String summary = advisory.getString("summary");

            System.out.println( "GHSA-" + ghsaId + " - " + cveId + " - " + url);

            // Add each row to the markdown table
            markdownTable.append("| ")
                         .append(ghsaId).append(" | ")
                         .append(cveId).append(" | ")
                         .append(url).append(" | ")
                         .append(summary).append(" | ")
                         .append(severity).append(" | ");

            // Extract vulnerabilities array
            JSONArray vulnerabilities = advisory.getJSONArray("vulnerabilities");
            for (int j = 0; j < vulnerabilities.length(); j++) {
                JSONObject vulnerability = vulnerabilities.getJSONObject(j);
                JSONObject packageInfo = vulnerability.getJSONObject("package");
                ecosystem = packageInfo.getString("ecosystem");
                name = packageInfo.getString("name");
                vulnerableVersionRange = vulnerability.getString("vulnerable_version_range");
                firstPatchedVersion = vulnerability.optString("first_patched_version", "N/A");
            }

            // Add each row to the markdown table
            markdownTable.append(ecosystem).append(" | ")
                         .append(name).append(" | ")
                         .append(vulnerableVersionRange).append(" | ")
                         .append(firstPatchedVersion).append(" |\n");
    }
    return markdownTable.toString();
}

}
