import java.util.*;

class Node {

    int assetId;
    String infrastructureName;
    String location;
    String maintenanceStatus;

    Node left, right;
    int height;

    Node(int assetId, String infrastructureName,
         String location, String maintenanceStatus) {

        this.assetId = assetId;
        this.infrastructureName = infrastructureName;
        this.location = location;
        this.maintenanceStatus = maintenanceStatus;

        height = 1;
    }
}

public class UrbanAIAVL {

    Node root;

    int height(Node node) {

        if (node == null)
            return 0;

        return node.height;
    }

    int getBalance(Node node) {

        if (node == null)
            return 0;

        return height(node.left) - height(node.right);
    }

    Node rightRotate(Node y) {

        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node leftRotate(Node x) {

        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    Node insert(Node node, int assetId,
                String infrastructureName,
                String location,
                String maintenanceStatus) {

        if (node == null)
            return new Node(assetId,
                    infrastructureName,
                    location,
                    maintenanceStatus);

        if (assetId < node.assetId)

            node.left = insert(node.left, assetId,
                    infrastructureName,
                    location,
                    maintenanceStatus);

        else if (assetId > node.assetId)

            node.right = insert(node.right, assetId,
                    infrastructureName,
                    location,
                    maintenanceStatus);

        else
            return node;

        node.height = 1 + Math.max(height(node.left),
                                   height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && assetId < node.left.assetId)
            return rightRotate(node);

        if (balance < -1 && assetId > node.right.assetId)
            return leftRotate(node);

        if (balance > 1 && assetId > node.left.assetId) {

            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && assetId < node.right.assetId) {

            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    Node search(Node root, int assetId) {

        if (root == null || root.assetId == assetId)
            return root;

        if (assetId < root.assetId)
            return search(root.left, assetId);

        return search(root.right, assetId);
    }

    Node minValueNode(Node node) {

        Node current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    Node deleteNode(Node root, int assetId) {

        if (root == null)
            return root;

        if (assetId < root.assetId)

            root.left = deleteNode(root.left, assetId);

        else if (assetId > root.assetId)

            root.right = deleteNode(root.right, assetId);

        else {

            if ((root.left == null) || (root.right == null)) {

                Node temp;

                if (root.left != null)
                    temp = root.left;
                else
                    temp = root.right;

                if (temp == null) {

                    temp = root;
                    root = null;
                }
                else
                    root = temp;
            }
            else {

                Node temp = minValueNode(root.right);

                root.assetId = temp.assetId;
                root.infrastructureName = temp.infrastructureName;
                root.location = temp.location;
                root.maintenanceStatus = temp.maintenanceStatus;

                root.right = deleteNode(root.right, temp.assetId);
            }
        }

        if (root == null)
            return root;

        root.height = Math.max(height(root.left),
                               height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        if (balance > 1 && getBalance(root.left) < 0) {

            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        if (balance < -1 && getBalance(root.right) > 0) {

            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    void inorderTraversal(Node node) {

        if (node != null) {

            inorderTraversal(node.left);

            System.out.println("----------------------------------");
            System.out.println("Asset ID            : " + node.assetId);
            System.out.println("Infrastructure Name : " + node.infrastructureName);
            System.out.println("Location            : " + node.location);
            System.out.println("Status              : " + node.maintenanceStatus);

            inorderTraversal(node.right);
        }
    }

    int totalAssets(Node node) {

        if (node == null)
            return 0;

        return 1 + totalAssets(node.left)
                 + totalAssets(node.right);
    }

    public static void main(String[] args) {

        UrbanAIAVL city = new UrbanAIAVL();

        city.root = city.insert(city.root, 120,
                "Traffic Signal",
                "Central Junction",
                "Active");

        city.root = city.insert(city.root, 210,
                "Metro Station",
                "North Zone",
                "Operational");

        city.root = city.insert(city.root, 95,
                "Road Sensor",
                "Highway Sector",
                "Under Maintenance");

        city.root = city.insert(city.root, 300,
                "Emergency Control Unit",
                "City Hospital",
                "Active");

        city.root = city.insert(city.root, 180,
                "Smart Street Light",
                "Downtown",
                "Operational");

        System.out.println("\n====== URBANAI INFRASTRUCTURE RECORDS ======\n");

        city.inorderTraversal(city.root);

        System.out.println("\n====== SEARCH RESULT ======\n");

        Node found = city.search(city.root, 210);

        if (found != null) {

            System.out.println("Infrastructure Asset Found!");
            System.out.println("----------------------------------");
            System.out.println("Asset ID            : " + found.assetId);
            System.out.println("Infrastructure Name : " + found.infrastructureName);
            System.out.println("Location            : " + found.location);
            System.out.println("Status              : " + found.maintenanceStatus);
        }

        System.out.println("\nDeleting Asset ID 95...\n");

        city.root = city.deleteNode(city.root, 95);

        System.out.println("====== UPDATED RECORDS ======\n");

        city.inorderTraversal(city.root);

        System.out.println("\n====== SMART CITY ANALYTICS ======\n");

        System.out.println("Total Active Infrastructure Assets : "
                + city.totalAssets(city.root));
    }
}
