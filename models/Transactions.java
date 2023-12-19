package models;

import java.time.LocalDateTime;
import java.util.*;

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

    public HashMap<String, Integer> getRankingOfMostSoldPhoneModelsLastThreeMonths() {
        System.out.println("Ranking of most sold phone models last three months");
        ArrayList<Transaction> soldTransactions = new ArrayList<>();
        transactions.stream()
                .filter(transaction -> transaction.transactionType == TRANSACTION_TYPE.SALE &&
                        transaction.timestamp.isAfter(LocalDateTime.now().minusMonths(3)))
                .forEach(soldTransactions::add);

        HashMap<String, Integer> phoneModelSales = getPhoneModelSales(soldTransactions);

        // rank the phone models by sales (use LinkedHashMap to keep the order descending - HashMap forces ascending)
        LinkedHashMap<String, Integer> rankedPhoneSales = phoneModelSales.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );

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
        private final LocalDateTime timestamp;
        private final TRANSACTION_TYPE transactionType;
        private final String brandName;
        private final String modelName;
        private final int stock;

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
