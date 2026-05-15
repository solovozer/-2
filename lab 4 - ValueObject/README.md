# Value Objects

**Задача(Value Object):** Один банк предоставляет возможность открытия счетов в различных иностранных валютах и ​​осуществляет управление переводами между этими счетами. Необходимо обеспечить, чтобы переводы могли выполняться исключительно между счетами, открытыми в одной и той же иностранной валюте.
<br>

## Без применения Value Object:
Тип валюта будет создан и сохранен в объеке константом. Денежная стоимость после каждой транзакции будет изменяться непосредственно с помощью методов `add()` и `subtract()`.

```java

public class Money {
    private BigDecimal amount; 
    private final String currency;

    public void add(Money other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount = this.amount.add(other.getAmount());
    }

    public void subtract(Money other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount = this.amount.subtract(other.getAmount());
    }

}
```

Потом этот класс используется для создания аккоунта.

```java
public class Account {
    private String id;
    private String userId;
    private Money balance;


    public Account(String id, String userId, Money balance) {} //...
}
```


При использовании этого подхода любое изменение значения в объекте влечет за собой изменение её значения во всех связанных с ним классах. Любой класс обладает полномочиями хранить экземпляр класса `Money`, использовать его для создания счета (представляющего собой класс `Account`), а затем напрямую изменять значение внутри класса `Money`  — и всё это без ведома самого класса `Account` о произошедшем изменении. В то же время другие классы могут установить значение `Money` отрицательным путём вычитания.
</br>

## С применением Value Object:
Класс `Money` хранит тип валюты и сумму — два атрибута, являющихся константами. Изменение значения подразумевает создание нового объекта `Money` с новыми значениями. Это гарантирует, что любое изменение внутренних данных осуществляемо тольно через класс `Account`.

```java
public final class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) { //...
    }
    //...

    public Money add(Money amount) {
        if (!this.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.getAmount().add(amount.getAmount()), this.getCurrency());
    }

    public Money subtract(Money amount) {
        if (!this.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.getAmount().subtract(amount.getAmount()), this.getCurrency());
    }
}
```

Ниже представлена диаграмма классов применения Value Object
![Рисунок 2: Диаграмма классов второго применения](./Assets/class%20d%206.jpg)

Этот подход обеспечивает инкапсуляцию в рамках ООП. Кроме того, сборщик мусора Java эффективно обрабатывает устаревшие объекты.


## Сравнение подходов

| Характеристика | Подход 1: Наследование и Мутабельность | Подход 2: Value Object (Immutability) |
| :--- | :--- | :--- |
| **Изменяемость** | **Mutable** (изменяет состояние текущего объекта) | **Immutable** (создает новый объект при операциях) |
| **Безопасность** | Риск побочных эффектов и "скрытых" изменений | Полная потокобезопасность и предсказуемость |
| **Расширяемость** | Сложно (нужно менять Factory и классы) | Легко (управление через константы данных) |
| **Целостность** | Ссылки на объект могут привести к багам | Объект-значение невозможно повредить |
| **Память** | Экономит объекты, но усложняет логику | Создает временные объекты (оптимально для GC) |


Ссылка на проект:  [https://github.com/solovozer/-2](https://github.com/solovozer/-2)
