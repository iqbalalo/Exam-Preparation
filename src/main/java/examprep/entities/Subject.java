package examprep.entities;

import java.util.Objects;

public class Subject {

    String id;
    String name;
    Integer totalMcq = 0;

    public Subject() {
    }

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(String id, String name, Integer totalMcq) {
        this.id = id;
        this.name = name;
        this.totalMcq = totalMcq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalMcq() {
        return totalMcq;
    }

    public void setTotalMcq(Integer totalMcq) {
        this.totalMcq = totalMcq;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subject other = (Subject) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
