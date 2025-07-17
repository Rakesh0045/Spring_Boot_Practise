package AutoWiringByType;

public class Specification {
    private String processor;
    private String modelName;

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "processor='" + processor + '\'' +
                ", modelName='" + modelName + '\'' +
                '}';
    }
}
