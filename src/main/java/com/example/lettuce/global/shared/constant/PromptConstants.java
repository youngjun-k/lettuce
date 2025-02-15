package com.example.lettuce.global.shared.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PromptConstants {

    public static final String CARBON_FOOTPRINT_PROMPT = """
                Please analyze the following image and extract the relevant details:

                1. itemCategory: Determine the category the object falls into, such as 'Second Hand Item Purchase', 'Clothing', 'Electronics', etc.
                2. itemName: Identify the specific name of the item, if available, based on the content in the image.
                3. description: Provide a concise description of the object or scene in the image. For example, if it's a second-hand item, mention the type of item and any notable details such as its condition or brand.
                4. savedCarbonFootprint: Calculate the approximate amount of CO₂ saved based on the type of item purchased, particularly for second-hand items. For example, '20kg CO₂ saved for second-hand clothing'.
                5. awardedPoint: Based on the type of activity, rate from 1 to 10, how much it contributes to carbon reduction. For example, recycling, buying second-hand items, and sustainable farming would have higher ratings.

                Respond in JSON format. Don't include code block like ```json or ```",
                The response should be returned in the following format:

                {
                    "itemCategory": "Second Hand Item Purchase",
                    "itemName": "Linen Flower Dress",
                    "description": "A second-hand linen flower dress from Hongdae Thrift Shop",
                    "savedCarbonFootprint": 20.00,
                    "awardedPoint": 8
                }

                If any information cannot be identified or extracted from the image, please leave that field empty or indicate that it's 'Unable to determine'.
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
