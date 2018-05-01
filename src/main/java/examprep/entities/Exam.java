package examprep.entities;

import com.google.common.base.Objects;
import java.util.List;

public class Exam implements Comparable<Exam> {

    String id;
    String name;
    List<Subject> subjects;
    String access;
    String sl;

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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name).toString();
    }

    @Override
    public int compareTo(Exam o) {
        Long thisId = Long.parseLong(sl);
        Long thatId = Long.parseLong(o.sl);

        if (thisId.equals(thatId)) {
            return 0;
        }
        else if (thisId > thatId) {
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + java.util.Objects.hashCode(this.id);
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
        final Exam other = (Exam) obj;
        if (!java.util.Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
