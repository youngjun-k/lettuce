package com.example.lettuce.global.shared.constant;

public class EmailConstants {
    public static final String WELCOME_EMAIL_SUBJECT = "(Email Verification Required) Welcome to Lettuce - Get Surplus by reducing carbon footprint";
    public static final String WELCOME_EMAIL_HTML = """
                <div style="font-family: 'Helvetica Neue', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 30px; background-color: #f9f9f9; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);">
                    <!-- Logo Image -->
                    <div style="text-align: center; margin-bottom: 20px;">
                        <img src="cid:logo.png" alt="Lettuce Logo" style="width: 150px; height: auto; max-width: 100%;">
                    </div>

                    <!-- Welcome Message -->
                    <p style="font-size: 18px; line-height: 1.7; margin-bottom: 20px; color: #333; text-align: center;">
                        Welcome to Lettuce! We're thrilled to have you join our community. Together, we'll work towards reducing carbon footprints and creating a more sustainable future.
                    </p>

                    <!-- Character Image -->
                    <div style="text-align: center; margin-bottom: 30px;">
                        <img src="cid:character.png" alt="Lettuce Main Character" style="width: 160px; height: 160px; border-radius: 50%; border: 4px solid #4CAF50;">
                    </div>

                    <!-- Call to Action -->
                    <p style="font-size: 16px; line-height: 1.6; margin-bottom: 30px; color: #555; text-align: center;">
                        To get started, please verify your email address by clicking the button below:
                    </p>

                    <div style="text-align: center; margin-bottom: 30px;">
                        <a href="${baseUrl}/api/auth/verify-email?token=${encodedToken}"
                           style="display: inline-block; padding: 12px 24px; background-color: #4CAF50; color: white; font-size: 16px; text-decoration: none; border-radius: 50px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); transition: background-color 0.3s ease;">
                            Verify Email
                        </a>
                    </div>

                    <!-- Support Information -->
                    <p style="font-size: 16px; line-height: 1.6; margin-bottom: 30px; color: #333; text-align: center;">
                        If you have any questions or need assistance, feel free to reach out to us at
                        <a href="mailto:support@lettuce.com" style="color: #4CAF50; text-decoration: none;">support@lettuce.com</a>.
                    </p>

                    <!-- Footer Message -->
                    <p style="font-size: 16px; line-height: 1.6; color: #555; text-align: center;">
                        Thank you for choosing Lettuce! Together, we're making a difference.
                    </p>
                </div>
            """;

    public static final String VERIFY_EMAIL_SUBJECT = "Verify Your Email Address";
    public static final String VERIFY_EMAIL_HTML = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <img src=\"cid:logo.png\" alt="Lettuce Logo" style="width: 300px; height: 112px;">
                <h1 style="color: #333; font-size: 24px; margin-bottom: 20px;">Verify Email</h1>
                <img src=\"cid:character.png\" alt="Lettuce Main Character" style="width: 160px; height: 160px;">
                <p style="font-size: 16px; line-height: 1.6; margin-bottom: 20px;">
                    Please verify your email address by clicking the link below:
                </p>
                <a href="${baseUrl}/api/auth/verify-email?token=${encodedToken}" style="display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">
                    Verify Email
                </a>
            </div>
            """;

    public static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset Password";
    public static final String RESET_PASSWORD_EMAIL_HTML = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <img src=\"cid:logo.png\" alt="Lettuce Logo" style="width: 300px; height: 112px;">
                <h1 style="color: #333; font-size: 24px; margin-bottom: 20px;">Reset Password</h1>
                <img src=\"cid:character.png\" alt="Lettuce Main Character" style="width: 160px; height: 160px;">
                <p style="font-size: 16px; line-height: 1.6; margin-bottom: 20px;">
                    Please reset your password by clicking the link below:
                </p>
                <a href="${baseUrl}/api/auth/reset-password?token=${encodedToken}" style="display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">
                    Reset Password
                </a>
            </div>
            """;
}
