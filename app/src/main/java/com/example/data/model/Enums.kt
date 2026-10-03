package com.example.data.model

enum class Platform(val label: String, val shortName: String) {
    CONSOLE("PlayStation / Xbox", "Console"),
    PC("PC (Origin / Steam)", "PC")
}

enum class TradingSignal(val label: String, val badgeColorHex: Long) {
    STRONG_BUY("COMPRA FUERTE", 0xFF00E676),
    BUY("COMPRAR", 0xFF69F0AE),
    HOLD("MANTENER", 0xFFFFD54F),
    SELL("VENDER", 0xFFFF8A80),
    PANIC_SELL("VENTA INMEDIATA", 0xFFFF5252)
}

enum class RiskLevel(val label: String, val colorHex: Long) {
    LOW("Riesgo Bajo", 0xFF00E676),
    MEDIUM("Riesgo Medio", 0xFFFFD54F),
    HIGH("Riesgo Alto", 0xFFFF5252)
}

enum class ArbitrageType(val title: String, val shortDesc: String) {
    CROSS_SOURCE("Arbitraje Futbin vs FUT.GG", "Desfase entre fuentes de precios"),
    SNIPING_FILTER("Filtro Sniping Rápido", "Precio por debajo del Lowest BIN"),
    CHEM_STYLE("Estilo de Química (Cazador/Sombra)", "Meta chem boost subvalorado"),
    FODDER_SBC("Medias SBC en Mínimos", "Pánico pre-SBC con rebote asegurado"),
    PANIC_REBOUND("Rebote de Pánico", "Caída exagerada por filtraciones"),
    OUT_OF_PACKS("Fuera de Sobres (OOP)", "Versión oro retenida por promo activa")
}

enum class NewsCategory(val label: String, val iconName: String) {
    PROMO_LEAK("Filtración Promo", "local_fire_department"),
    TOTW_PREDICTION("Predicción TOTW", "stars"),
    SBC_EVENT("Evento SBC", "inventory_2"),
    CRASH_ALERT("Alerta Mercado", "trending_down"),
    EVOLUTIONS("Evoluciones Meta", "bolt")
}
