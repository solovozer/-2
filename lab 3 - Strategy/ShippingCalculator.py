import Strategies
from Strategies import ShippingInfo


class ShippingCalculator:
    def __init__(self, strategy: Strategies): self._strategy = strategy

    @property
    def strategy(self) -> Strategies: return self._strategy

    @strategy.setter
    def strategy(self, strategy: Strategies): self._strategy = strategy

    def calculate_price(self, info: ShippingInfo) -> float:
        price = self._strategy.implement_strategies(info)
        return round(price, 2)