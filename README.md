# Верификатор автоматов Харела с помощью LTL формул
Проект был выполнен Лободой А. и Хованским В. в рамках курса по верификации кафедры «Комьютерных технологий» Университета ИТМО.

## LTL 2 Buchi Automaton Utility
В программе используется сторонний транслятор LTL-формул в автоматы Бюхи под названием ltl2ba (http://www.lsv.fr/~gastin/ltl2ba/), собранный
под Windows. Перед работой стоит удостовериться, что транслятор `ltl2ba/ltl2ba.exe` работает из корневой директории приложения без проблем.

## Запуск
Для запуска программы нужна Oracle JRE 8, которую нужно предварительно установить с сайта http://www.oracle.com/technetwork/java/javase/downloads/index.html и убедиться, что она работает:

> java --version

### Интерфейс верификатора

* `.\run.bat` - запускает программу и сразу же выводит help;
* `.\run.bat -h` - аналогично предыдущему;
* `.\run.bat -m .\data\AChart.xstd -f .\data\AChart.ltl.correct` - запускает верификатор на модели AChart и проверяет все LTL формулы из файла `data/AChart.ltl.correct`;
* `.\run.bat -m .\data\AChart.xstd -l "true"` - проверяет формулу, заданную параметром на модели AChart.

## Синтаксис LTL формул 
Формулы содержат следующие операторы `!`, `X`, `G`, `F`, `R`, `U`, `&`, `|`, `->`, `<->`. Также доступны скобки и значения `true` и `false`. Переменные записываются в одинарных или двойных кавычках.

Операторы записаны в порядке убывания приоритета, то есть `'a' | 'b' & 'c'` будет прочитано как `'a' | ('b' & 'c')`.

### Примеры
* `'S(Error)'` - состояние Error исходного автомата;
* `'E(Token)'`, `'A(print)'` - для входных и выходных воздействий соответственно. 

Примеры формул можно посмотреть в файле `data/LTLParser.ltl.correct` или других файлах `data/*.ltl.*`.

## Сборка
Сборка осуществляется с помощью утилиты https://maven.apache.org/ следующей командой:

> mvn clean package

## Тестирование и визуализация данных
Для успешного прохождения всех автоматических тестов и для визуализации исходных и преобразованных автоматов требуется установленная утилита http://www.graphviz.org/. Программа должна быть доступна для глобального вызова с помощью команды:

> dot

После этого можно запустить автоматические тесты командой:
> mvn test

В результате прохождения тестов будут сгенерированы pdf изображения исходных автоматов или автоматов Бюхи, полученных после преобразования из автоматов Харела. Посмотреть их будет можно в директориях `temp/diagram` и `temp/graph` соответственно.
