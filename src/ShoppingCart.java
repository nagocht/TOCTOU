import java.io.IOException;
import java.util.Scanner;

public class ShoppingCart {

    public static void main(String[] args) throws Exception {
        Wallet wallet = null;
        Pocket pocket = null;

        try {
            wallet = new Wallet();
            pocket = new Pocket();


            System.out.println(printBalance(wallet));

            printProductList();

            String article = chooseArticle();
            checkCredits(article, wallet);
            int newBalance = safewithdrawMoney(article, wallet);
            addArticleToPocket(article, pocket);
            System.out.println(String.format("Your balance: %d credits", newBalance));

        } finally {
            if (pocket!=null) {
                pocket.close();
            }
            if (wallet!=null) {
                wallet.close();
            }
        }


    }

    private static String printBalance(Wallet userWallet) throws IOException {
        int balance = userWallet.getBalance();
        return String.format("Your balance: %d credits", balance);
    }

    private static void printProductList(){
        System.out.println(Store.asString());
    }

    private static String chooseArticle() {
        Scanner scan = new Scanner(System.in);
        System.out.println("What do you want to buy?:");

        return scan.next();
    }

    private static int checkCredits(String article, Wallet userWallet) throws IOException {
        int price = Store.getProductPrice(article);
        int balance = userWallet.getBalance();

        int newBalance = balance - price;
        if (newBalance < 0) {
            System.out.println("Not enough credit!");
            System.exit(0);
        }
        System.out.println("Buying Article in progress!");
        return balance;
    }

    private static int safewithdrawMoney(String article, Wallet userWallet) throws Exception {
        int price = Store.getProductPrice(article);
        userWallet.safeWithdraw(price);
        return userWallet.getBalance();
    }

    private static void addArticleToPocket(String article, Pocket userPocket) throws Exception {
        userPocket.safeAddProduct(article);
    }
}
