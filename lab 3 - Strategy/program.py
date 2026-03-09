from Strategies import *
from ShippingCalculator import *


shipment = ShippingInfo(distance=750, weight=2.5)


calculators = [
    ShippingCalculator(CDEKStrategies()),
    ShippingCalculator(PochtaRuStrategies()),
    ShippingCalculator(YandexStrategies())
]
optimal_calc = min(calculators, key=lambda c: c.calculate_price(shipment))

print(f"Optimal Strategy: {optimal_calc.strategy.__class__.__name__} at {optimal_calc.calculate_price(shipment)} ₽")
