package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Transactions {

    private final ArrayList<Transaction> transactions = new ArrayList<>();
    public void showTransactions() {
        System.out.println("Transactions");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public void logTransaction(String brandName, String modelName, int stock, TRANSACTION_TYPE transactionType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Transaction transaction = new Transaction(localDateTime, transactionType, brandName, modelName, stock);
        transactions.add(transaction);
    }

    // method dedicated to getting the Ranking Of Most Sold Phone Models in Last Three Months
    public HashMap<String, Integer> getRankingOfMostSoldPhoneModelsLastThreeMonths() {
        System.out.println("Ranking of most sold phone models last three months");
        ArrayList<Transaction> soldTransactions = new ArrayList<>();
        transactions.stream()
                .filter(transaction -> transaction.transactionType == TRANSACTION_TYPE.SALE &&
                        transaction.timestamp.isAfter(LocalDateTime.now().minusMonths(3)))
                .forEach(soldTransactions::add);

        HashMap<String, Integer> phoneModelSales = getPhoneModelSales(soldTransactions);

        // rank the phone models by sales (in descending order)
        HashMap<String, Integer> rankedPhoneSales = phoneModelSales.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);

        return rankedPhoneSales;
    }

    private static HashMap<String, Integer> getPhoneModelSales(ArrayList<Transaction> soldTransactions) {
        HashMap<String, Integer> phoneModelSales = new HashMap<>();
        for (Transaction soldTransaction : soldTransactions) {
            String modelName = soldTransaction.modelName;
            int phonesSoldInTransaction = soldTransaction.stock;
            if (phoneModelSales.containsKey(modelName)) {
                int totalPhonesSold = phoneModelSales.get(modelName);
                phoneModelSales.put(modelName, totalPhonesSold + phonesSoldInTransaction);
            } else {
                phoneModelSales.put(modelName, phonesSoldInTransaction);
            }
        }
        return phoneModelSales;
    }

    static class Transaction {
        private LocalDateTime timestamp;
        private TRANSACTION_TYPE transactionType;
        private String brandName;
        private String modelName;
        private int stock;

        public Transaction(LocalDateTime timestamp, TRANSACTION_TYPE transactionType,
                           String brandName, String modelName, int stock) {
            this.timestamp = timestamp;
            this.transactionType = transactionType;
            this.brandName = brandName;
            this.modelName = modelName;
            this.stock = stock;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "timestamp=" + timestamp +
                    ", transactionType=" + transactionType +
                    ", brandName='" + brandName + '\'' +
                    ", modelName='" + modelName + '\'' +
                    ", stock=" + stock +
                    '}';
        }
    }
}
