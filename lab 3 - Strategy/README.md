# Shippro

**Задача(Strategy):** Существует три транспортные компании: CDEK, Yandex и Pochta, каждая из которых использует разные методы ценообразования в зависимости от расстояния и веса. Определите оптимальную транспортную компанию для заданного расстояния и веса груза.

**Решение:** Используя Strategy, мы создадим класс для каждой судоходной компании, содержащий cвой метод для расчета цены. Все три класса будут наследовать от абстрактного класса под названием Strategy. 
</br>

Ниже представлена ​​диаграмма классов реализации.
![Рисунок 1: Диаграмма классов реализации](./Assets/Class.png)

</br>
А ниже фрагмент кода для классов

```python
class Strategies(ABC):
    def __init__(self, name): self.name = name
    @abstractmethod
    def implement_strategies(self, info: ShippingInfo): pass

class PochtaRuStrategies(Strategies):
    def __init__(self): super().__init__("Pochta.ru")
    def implement_strategies(self, info: ShippingInfo): #...

class CDEKStrategies(Strategies):
    def __init__(self): super().__init__("cdek.ru")
    def implement_strategies(self, info: ShippingInfo): #...

class YandexStrategies(Strategies):
    def __init__(self): super().__init__("Yandex.ru")
    def implement_strategies(self, info: ShippingInfo): #...
```
</br>


А класс ShippingCalculator, получающий на вход список стратегий, будет отвечать за тестирование каждой стратегии и выбор наиболее эффективной из них.

```python
class ShippingCalculator:
    def __init__(self, strategies: List[Strategies.Strategies]): 
        self._strategies = strategies

    def calculate_price(self, info: ShippingInfo):
        #...
        for strategy in self._strategies: #...
```
</br>
Этот класс приложения будет реализован при вызове функции calculate_price().

```python
strategies = [
    CDEKStrategies(),
    PochtaRuStrategies(),
    YandexStrategies(),
]
calculator = ShippingCalculator(strategies=strategies)
best = calculator.calculate_price(#...)
```

</br>

На рисунке 2 представлена ​​диаграмма последовательностей для update_price().
![up](./Assets/cp.png)

</br>

## Без применения Strategy:
Нам сначала пришлось бы определить каждую компанию с помощью функции. Затем нам пришлось бы добавить условия if... else... в класс ShippingCalculator и вычислить их. На рисунке 3 показан код, в котором шаблон Strategy  не применяется.

```python

def pochtaru(info : ShippingInfo):#...
def cdek(info: ShippingInfo):#...
def yandex(info : ShippingInfo):#...
```
</br>
ShippingCalculator:
```python
class ShippingCalculator:
    def calculate_price(self, info: ShippingInfo):
        #...
        if optimal == poc: return CompanyInfo("pochta.ru", float(poc))
        if optimal == cde: return CompanyInfo("cdek.ru", float(cde))
        if optimal == yan: return CompanyInfo("yandex.ru", float(yan))
```

На рисунке 3 представлена GUI приложения
![GUI](./Assets/UI.png)

# Заключение

Паттерн Strategy преобразует ShippingCalculator из жесткого, монолитного скрипта в гибкий механизм выполнения. Разделение логики вычислений от класса калькулятора позволяет исключить уязвимые условные блоки. Такой подход соответствует принципу открытости/закрытости, позволяя подключать новых поставщиков услуг доставки во время выполнения, просто добавляя их в список стратегий. В конечном итоге, это обеспечивает модульную, тестируемую и масштабируемую архитектуру, которая обрабатывает сложные бизнес-правила с минимальным дублированием кода.


| Особенности | Без Strategy (закодировано жестко) | С использованием Strategy |

| :--- | :--- | :--- |
| **Принцип открытости/закрытости** | **Нарушено.** Необходимо изменять класс `ShippingCalculator` каждый раз при добавлении нового перевозчика. | **Соблюдено.** Новые перевозчики добавляются путем создания новых классов стратегий без изменения существующего кода. |
| **Поддерживаемость** | **Низкая.** Большие цепочки `if/elif` превращаются в «спагетти-код», который трудно читать и который подвержен ошибкам. | **Высокая.** Логика для каждого поставщика изолирована в собственном классе, что делает кодовую базу модульной и чистой. |
| **Тестируемость** | **Сложная.** Тестирование одного перевозчика требует выполнения всего блока условий, что делает модульные тесты ненадежными. | **Простая.** Каждый класс стратегии может быть протестирован независимо в полной изоляции. |

| **Масштабируемость** | **Статическая.** Добавление 10-го или 20-го перевозчика делает метод `calculate_price` громоздким и неуправляемым. | **Динамическая.** Калькулятор остается компактным независимо от количества стратегий, добавленных в список. |



Ссылка на проект:  [https://github.com/solovozer/-2](https://github.com/solovozer/-2)
