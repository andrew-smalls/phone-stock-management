# phone-stock-management

## Description
This is a simple phone stock management system. It is possible to add, update, delete and list phoneModels. It is also possible to search for phoneModels by name, brand and price.

## Usage
1. Download the JAR file from [here](https://some-link.com).
2. Prepare an "instructions.txt" file with the following format:
```
add Nokia XR21 10
add Samsung S22 5
list
add iPhone 15 2
add Xiaomi 11 3
list
```
3. Run the JAR file with the following command: `java -jar phone-stock-management.jar instructions.txt`
4. The output will be printed to the console:
```
Executing add[Nokia, XR21, 10]
Executing add[Samsung, S22, 5]
Executing list
________________________________________________________________
|Brand               |Model                         |Quantity  |
|--------------------|------------------------------|----------|
|Nokia               |XR21                          |10        |
|Samsung             |S22                           |5         |
‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾

Executing add[iPhone, 15, 2]
Executing add[Xiaomi, 11, 3]
Executing list
________________________________________________________________
|Brand               |Model                         |Quantity  |
|--------------------|------------------------------|----------|
|Nokia               |XR21                          |10        |
|Samsung             |S22                           |5         |
|iPhone              |15                            |2         |
|Xiaomi              |11                            |3         |
‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
```