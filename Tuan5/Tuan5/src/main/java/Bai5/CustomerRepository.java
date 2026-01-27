package Bai5;

import java.util.HashSet;
import java.util.Set;

public class CustomerRepository {
    private final Set<String> existingIds = new HashSet<>();
    private final Set<String> existingEmails = new HashSet<>();

    public CustomerRepository() {
        // giả lập dữ liệu đã tồn tại để test "trùng"
        existingIds.add("KH0001");
        existingEmails.add("old@email.com");
    }

    public boolean existsCustomerId(String id) {
        return existingIds.contains(id);
    }

    public boolean existsEmail(String email) {
        return existingEmails.contains(email);
    }

    public void save(Customer c) {
        existingIds.add(c.customerId);
        existingEmails.add(c.email);
    }
}
