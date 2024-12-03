package com.pluriverse.htmlgenerator.util;

public class ProjectConstants {

    public static final String DEFAULT_CSS = "/* Reset and Base Styles */\n" +
            "body {\n" +
            "    margin: 0;\n" +
            "    font-family: Arial, sans-serif;\n" +
            "    line-height: 1.6;\n" +
            "    background-color: #f9f9f9;\n" +
            "    color: #333;\n" +
            "}\n" +
            "\n" +
            ".document-container {\n" +
            "    width: 80%;\n" +
            "    margin: 0 auto;\n" +
            "    padding: 20px;\n" +
            "    background: #fff;\n" +
            "    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
            "    border-radius: 8px;\n" +
            "}\n" +
            "\n" +
            ".main-title {\n" +
            "    font-size: 2.5em;\n" +
            "    color: #2c3e50;\n" +
            "    margin-bottom: 20px;\n" +
            "}\n" +
            "\n" +
            ".subtitle {\n" +
            "    font-size: 1.8em;\n" +
            "    color: #34495e;\n" +
            "    margin-top: 20px;\n" +
            "    margin-bottom: 10px;\n" +
            "}\n" +
            "\n" +
            ".paragraph {\n" +
            "    font-size: 1.1em;\n" +
            "    line-height: 1.8;\n" +
            "    margin-bottom: 20px;\n" +
            "}\n" +
            "\n" +
            ".image-container {\n" +
            "    text-align: center;\n" +
            "    margin: 20px 0;\n" +
            "}\n" +
            "\n" +
            ".responsive-image {\n" +
            "    max-width: 100%;\n" +
            "    height: auto;\n" +
            "    border-radius: 5px;\n" +
            "    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
            "}\n" +
            "\n" +
            "/* General Styles for Code Snippets */\n" +
            ".code-container {\n" +
            "    display: flex;\n" +
            "    flex-wrap: wrap;\n" +
            "    justify-content: flex-start;\n" +
            "}\n" +
            "\n" +
            ".code-inline {\n" +
            "    background: #2d2d2d;\n" +
            "    color: #ffffff;\n" +
            "    font-family: \"Courier New\", Courier, monospace;\n" +
            "    font-size: 0.95em;\n" +
            "    border-radius: 4px;\n" +
            "    margin: 0;\n" +
            "    overflow-x: auto;\n" +
            "    box-sizing: border-box;\n" +
            "}\n" +
            "\n" +
            "@media (max-width: 768px) {\n" +
            "    .code-inline {\n" +
            "        max-width: 100%;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            ".ide-style {\n" +
            "    border-left: 5px solid #4CAF50;\n" +
            "}\n" +
            "\n" +
            ".console-style {\n" +
            "    border-left: 5px solid #FFC107;\n" +
            "}\n" +
            "\n" +
            ".unordered-list,\n" +
            ".ordered-list {\n" +
            "    margin: 20px 0;\n" +
            "    padding-left: 40px;\n" +
            "}\n" +
            "\n" +
            ".unordered-list li,\n" +
            ".ordered-list li {\n" +
            "    margin-bottom: 10px;\n" +
            "}\n" +
            "\n" +
            "@media (max-width: 375px) {\n" +
            "    .main-title {\n" +
            "        font-size: 1.8em;\n" +
            "    }\n" +
            "    .subtitle {\n" +
            "        font-size: 1.3em;\n" +
            "    }\n" +
            "    .paragraph {\n" +
            "        font-size: 0.9em;\n" +
            "        line-height: 1.6;\n" +
            "    }\n" +
            "}\n";
}
