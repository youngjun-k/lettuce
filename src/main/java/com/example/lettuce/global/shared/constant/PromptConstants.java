package com.example.lettuce.global.shared.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PromptConstants {

    public static final String RECIPE_RECOMMENDATION_PROMPT = """
            {
                "prompt": {
                    "task": "You are a nutritionist tasked with recommending a recipe tailored to a client's profile and specified ingredients.
                    You should respond in JSON format. Don't include code block like ```json or ```",
                    "input_format": {
                        "client_profile": {
                            "age": "%s",
                            "gender": "%s",
                            "height": "%s",
                            "weight": "%s",
                            "fitness_goal": "%s",
                        },
                        "recipe_recommendation_request": {
                            "ingredients": "%s",
                        },
                    },
                    "output_format": {
                        "recipeName": "Name of the recipe",
                        "ingredients": ["List of ingredients"],
                        "instructions": "Instructions on how to prepare the recipe",
                        "cookingTime": "Estimated cooking time",
                        "calories": "Estimated calories",
                        "carbohydrates": "Estimated carbohydrates",
                        "protein": "Estimated protein",
                        "fat": "Estimated fat",
                    },
                }
            }
                        """;

    public static final String RESTAURANT_RECOMMENDATION_PROMPT = """
            {
                "prompt": {
                    "task": "You are a nutritionist tasked with recommending a restaurant tailored to a client's profile, specified cuisine, and location.
                    You should respond in JSON format. Don't include code block like ```json or ```",
                    "input_format": {
                        "client_profile": {
                            "age": "%s",
                            "gender": "%s",
                            "height": "%s",
                            "weight": "%s",
                            "fitness_goal": "%s",
                        },
                        "restaurant_recommendation_request": {
                            "cuisine": "%s",
                            "latitude": "%s",
                            "longitude": "%s",
                        },
                    },
                    "output_format": {
                        "restaurants": [
                            {
                                "name": "Name of the restaurant",
                                "address": "Address of the restaurant",
                                "menus": ["List of menus"],
                            }
                        ]
                    }
                }
            }
            """;
}
