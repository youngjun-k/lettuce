package com.example.lettuce.global.shared.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PromptConstants {

    public static final String CARBON_FOOTPRINT_PROMPT = """
                Please analyze the following image and extract the relevant details:

                1. productName: Identify the specific name of the item, if available, based on the content in the image.
                2. productCategory: Identify the category of the item, if available, based on the content in the image.                
                3. carbonValue: Calculate carbon footprint of the product.
                4. carbonReduction: Calculate carbon reduction of the product.
                5. environmentalImpact: Provide a concise description of the environmental impact of the product.

                Respond in JSON format. Don't include code block like ```json or ```",
                The response should be returned in the following format:

                {
                    "productName": "Linen Flower Dress",
                    "productCategory": "Clothing",
                    "carbonValue": 20.00,
                    "carbonReduction": 10.00,
                    "environmentalImpact": "This product is made from organic materials and is environmentally friendly."
                }

                If the image is not clear or the product is not identifiable, return empty string for productName, productCategory, description, environmentalImpact and 0.00 for carbonValue, carbonReduction.
            """;

    public static final String CARBON_FOOTPRINT_BY_TEXT_PROMPT = """
                    Please analyze the following product name and return the approximate amount of carbon footprint of the product:

                    Product Name: {productName}

                    Respond in String format.
                    Example:
                    20.00
            """;

    public static final String CARBON_FOOTPRINT_BY_TEXT_PROMPT_LIST = """
                    Please analyze the following product names and return the approximate amount of carbon footprint of the products:

                    Product Names: {productNames}

                    Respond in String format.
                    Example:
                    20.00, 10.00, 30.00
            """;
}
