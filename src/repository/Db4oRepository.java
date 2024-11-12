package repository;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import db.DBConnection;
import model.Condition;

import java.util.ArrayList;
import java.util.List;

public class Db4oRepository<T> {
    private Class<T> type; // Lưu trữ kiểu đối tượng generic
    public ObjectContainer db;

    public Db4oRepository(Class<T> type, Integer activationDepth, Integer updateDepth, Boolean cascadeOnActivate) {
        this.type = type;
        DBConnection.openConnection("DBTopic2", activationDepth, updateDepth); // cấu hình activationDepth, updateDepth
        db = DBConnection.getConnection();
        if (cascadeOnActivate) {
            this.db.ext().configure().objectClass(type).cascadeOnActivate(true);
        }
    }

    // Thêm đối tượng vào database
    public void save(T entity) {
        db.store(entity); // Lưu đối tượng vào db4o
        db.commit(); // Commit để thay đổi có hiệu lực
    }

    public void saveAll(List<T> entityList) {
        entityList.stream().forEach(e -> {
            System.out.println("Save data:" + e);
            db.store(e); // Lưu đối tượng vào db4o
        });
        db.commit(); // Commit để thay đổi có hiệu lực
    }

    // Tìm đối tượng theo điều kiện
    public List<T> findByExample(T example) {
        ObjectSet<T> result = db.queryByExample(example); // Tìm đối tượng theo ví dụ
        return new ArrayList<>(result); // Trả về danh sách kết quả
    }

    // Lấy tất cả đối tượng từ database
    public List<T> findAll() {
        ObjectSet<T> result = db.query(type); // Query toàn bộ đối tượng theo kiểu
        return new ArrayList<>(result);
    }


    // Tìm kiếm với Native Query (NQ)
    public List<T> findByNativeQuery(Predicate<T> queryCondition) {
        ObjectSet<T> result = db.query(queryCondition);
        return new ArrayList<>(result);
    }

    // Tìm kiếm với SODA bằng Map<Condition>

    public List<T> findBySodaQuery(List<Condition> conditions) {
        Query query = db.query();
        query.constrain(type);

        for (Condition condition : conditions) {
            String[] fieldPath = condition.getFieldName().split("\\.");
            Object value = condition.getValue();
            String operator = condition.getOperator();

            Query fieldQuery = query;
            for (String field : fieldPath) {
                fieldQuery = fieldQuery.descend(field);
            }

            switch (operator) {
                case "=":
                    fieldQuery.constrain(value);
                    break;
                case "!=":
                    fieldQuery.constrain(value).not();
                    break;
                case ">":
                    fieldQuery.constrain(value).greater();
                    break;
                case ">=":
                    fieldQuery.constrain(value).greater().equal();
                    break;
                case "<":
                    fieldQuery.constrain(value).smaller();
                    break;
                case "<=":
                    fieldQuery.constrain(value).smaller().equal();
                    break;
                case "contains":
                    fieldQuery.constrain(new Evaluation() {
                        public void evaluate(Candidate candidate) {
                            Object fieldValue = candidate.getObject();
                            if (fieldValue instanceof String) {
                                candidate.include(((String) fieldValue).toLowerCase().contains(value.toString().toLowerCase()));
                            } else if (fieldValue instanceof List) {
                                for (Object item : (List<?>) fieldValue) {
                                    if (item.toString().toLowerCase().contains(value.toString().toLowerCase())) {
                                        candidate.include(true);
                                        return;
                                    }
                                }
                                candidate.include(false);
                            } else {
                                candidate.include(false);
                            }
                        }
                    });
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported operator: " + operator);
            }
        }

        ObjectSet<T> result = query.execute();
        return new ArrayList<>(result);
    }


    // Cập nhật đối tượng trong database
    public void update(T entity) {
        db.store(entity); // Lưu đối tượng đã cập nhật
        db.commit(); // Commit để thay đổi có hiệu lực
    }

    // Xóa đối tượng khỏi database
    public void delete(T entity) {
        db.delete(entity); // Xóa đối tượng
        db.commit(); // Commit để thay đổi có hiệu lực
    }

    // Làm mới đối tượng trong RAM với độ sâu được chỉ định
    public void refreshEntity(T entity, int depth) {
        db.ext().refresh(entity, depth); // Làm mới đối tượng và các đối tượng liên quan với độ sâu
    }

    // Đóng kết nối với database
    public void close() {
        db.close(); // Đóng kết nối db4o
    }

}
