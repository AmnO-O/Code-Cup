package com.example.codecup.data.database

import com.example.codecup.models.Product

/**
 * Initial menu inserted exactly once when the Room database is first created
 * (see [AppDatabase]'s seed callback). Product IDs are stable so features like
 * the Barista recommendations can reference them safely.
 */
object SeedData {

    private const val IMAGE_CAPPUCCINO =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAY5IjLa4trLoSdiHEcR_x70S-3GwqkjJKEXHdAJtb2o6U_dUTIy3uCnpZa6koN0hrThnARrqGwbwhjbY1j9RopDuD3ZOi9VQBrLeSGrzgTbV9LrksGNoFMbxpU09JUh4DDfc8y95Ni5ADg6QJvIK24mBiNtG3fF2ZHCcWSg2EngFe8WUH1OOQKppbLnNNlUe6EOPvbzYRmKuZhm1_EgQX-dPBag9IKjcPt_7zxC_VIlftmBUJg3kgH"
    private const val IMAGE_COLD_BREW =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBvuMOxfMlOtUFOqXv8rnaT1qyu8i7BScy-VAS3CbbNxYP1S9n5loieH3ewA-CAKky7ikUBrbE6wRUK-q9oU_CqrTzVLfBxCjJWuBXxCpwz-DMnpinP8XGz75kc14N8Gqdy7HbtZfKDZ09L3WbgqdPiWSe25_i0XZKFP4vuo45PdiolGh1G2N7gK90hgk6Wq5P4UU2eozryCOSMKMSOG_JwXq6XwZ3wCoNoTwMzVdhgiuL61byalPQ6"
    private const val IMAGE_MACCHIATO =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuDw5H0jYs_HcnZCSg0KkngaqH9OKHa9WKxZR5lLgpoVHk2wVHsy_2QOQw_gmw98PADS3OSnNdkIv1565vp5BRYIsjAC9j7FYfj_fI7uSB5Ufmo_mOwman0-aJPCrTxBy99xd0cngCJOleUsB0UdPIPnywFdhDy84_j_tomXuhLJ-5PGyEyYm6Rizq0iIMIpXX27akVExSGBkSeaES8cbaA2xuXRlGAnwbB8kRSnS1ypeax8DA_IvIJu"
    private const val IMAGE_CROISSANT =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCePvz8Wf36x6e0RrEh5Hc2h9C4ckAtGrTt31vswN6NAijYAD6FS92sP_uuFZhB6FFBv5z6YnHUNHEpnKZB4rZ5ySjd2_tWpbAQ-NQfwX-AOILd6K8oKic7A5NnvN7j1fboddJ1XXBjsGL0YAK6l8bQcz5KF70jrAbA2xQEoOAnhnT68ummOBHjuUhuwZmmfwEG9bMWW2Op6uuDv1_fRfCyaJcpgFKjipaXDVG3m8vg0G2pknwpC2cW"

    val products = listOf(
        Product(1, "Artisan Cappuccino", "Rich espresso layered with deeply frothed milk.", 4.50, IMAGE_CAPPUCCINO, "Espresso"),
        Product(2, "Nitro Cold Brew", "Smooth, creamy, and infused with nitrogen.", 5.00, IMAGE_COLD_BREW, "Cold Brew"),
        Product(3, "Caramel Macchiato", "Vanilla syrup, steamed milk, espresso, and caramel.", 5.50, IMAGE_MACCHIATO, "Latte"),
        Product(4, "Butter Croissant", "Flaky, buttery, baked fresh every morning.", 3.50, IMAGE_CROISSANT, "Pastries"),
        Product(5, "Classic Espresso", "A bold double shot with a golden crema.", 3.00, IMAGE_CAPPUCCINO, "Espresso"),
        Product(6, "Iced Americano", "Espresso over cold water and ice — crisp and clean.", 4.00, IMAGE_COLD_BREW, "Cold Brew"),
        Product(7, "Oat Milk Latte", "Silky oat milk poured over a smooth double shot.", 5.25, IMAGE_MACCHIATO, "Latte"),
        Product(8, "Almond Croissant", "Twice-baked with almond cream and toasted flakes.", 4.00, IMAGE_CROISSANT, "Pastries")
    )
}
