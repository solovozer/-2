from abc import ABC, abstractmethod


class ShippingInfo:
    def __init__(self, distance, weight):
        self.distance = distance 
        self.weight = weight      

class Strategies(ABC):
    def __init__(self, name): self.name = name
    @abstractmethod
    def implement_strategies(self, info: ShippingInfo): pass

class PochtaRuStrategies(Strategies):
    def __init__(self): super().__init__("Pochta.ru")
    def implement_strategies(self, info: ShippingInfo):
        base = 150
        rate = 30 if info.distance < 600 else 50
        return (base + (info.weight * rate)) * 1.01

class CDEKStrategies(Strategies):
    def __init__(self): super().__init__("cdek.ru")
    def implement_strategies(self, info: ShippingInfo):
        if info.weight <= 0.5: base = 350
        elif info.weight <= 2.0: base = 600
        elif info.weight <= 5.0: base = 950
        else: base = 1500
        multiplier = 1.0 if info.distance < 1000 else info.distance / 1000
        return (base * multiplier) * 1.008

class YandexStrategies(Strategies):
    def __init__(self): 
        super().__init__("Yandex.ru")
    def implement_strategies(self, info: ShippingInfo):
        base = 150
        km_rate = 25
        w = (info.weight * info.weight / 50)  
        return (base + (info.distance * km_rate)) + w
    


