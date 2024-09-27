package Labs.Lab_8.exercise_2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface XMLComponent {
    void addAttribute(String key, String value);

    String toXML(int level);

    default String indent(int level) {
        return "\t".repeat(level - 1);
    }
}

class XMLLeaf implements XMLComponent {
    private String tag;
    private String value;
    private Map<String, String> attributes;

    public XMLLeaf(String tag, String value) {
        this.tag = tag;
        this.value = value;
        attributes = new LinkedHashMap<>();
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);

    }

    @Override
    public String toXML(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent(indentLevel)).append("<").append(tag);
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            sb.append(" ").append(attribute.getKey()).append("=\"").append(attribute.getValue()).append("\"");
        }

        sb.append(">").append(value).append("</").append(tag).append(">");
        return sb.toString();

    }
}

class XMLComposite implements XMLComponent {
    private String tag;
    private Map<String, String> attributes;
    private List<XMLComponent> components;

    public XMLComposite(String tag) {
        this.tag = tag;
        this.attributes = new LinkedHashMap<>();
        this.components = new ArrayList<>();
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void addComponent(XMLComponent component) {
        components.add(component);
    }

    @Override
    public String toXML(int indentLevel) {
        StringBuilder xml = new StringBuilder();
        xml.append(indent(indentLevel)).append("<").append(tag);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            xml.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
        xml.append(">").append("\n");
        for (XMLComponent component : components) {
            xml.append(component.toXML(indentLevel + 1)).append("\n");
        }
        xml.append(indent(indentLevel)).append("</").append(tag).append(">");
        return xml.toString();
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase == 1) {
            //TODO Print the component object
            System.out.println(component.toXML(1));
        } else if (testCase == 2) {
            //TODO print the composite object
            System.out.println(composite.toXML(1));
        } else if (testCase == 3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level", "1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level", "2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level", "3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            //TODO print the main object
            System.out.println(main.toXML(1));
        }
    }
}
