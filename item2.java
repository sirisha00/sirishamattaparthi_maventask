public class ItemGiftParser {

    private static final Logger LOG = Logger.getLogger(ItemGiftParser.class);

    private DocumentBuilder builder;

    private XPath path;

    
    public ItemGiftParser() throws CreateDocumentConfigurationException {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            builder = documentFactory.newDocumentBuilder();

            XPathFactory xpathFactory = XPathFactory.newInstance();
            path = xpathFactory.newXPath();
        } catch (ParserConfigurationException e) {
            throw new CreateDocumentConfigurationException("exception create new document", e);
        }
    }

   
    @SuppressWarnings("unchecked")
    public ArrayList<Sweets> parse(String fileName) throws XmlParseException {

        ArrayList<Sweets> items = new ArrayList<Sweets>();
        try {
            File file = new File(fileName);
            Document doc;
            try {
                doc = builder.parse(file);
            } catch (Exception e) {
                LOG.error("xml file not found", e);
                throw new XmlNotFoundException("xml file wasn't found", e);
            }

            int itemCount = Integer.parseInt(path.evaluate("count(/gift/item)", doc));

            for (int i = 1; i <= itemCount; i++) {

                double sugar = Double.parseDouble(path.evaluate("/gift/item[" + i + "]/@sugar", doc));
                String name = path.evaluate("/gift/item[" + i + "]/name", doc);
                double weight = Double.parseDouble(path.evaluate("/gift/item[" + i
                        + "]/weight", doc));

                @SuppressWarnings("rawtypes")
                Class cl = Class.forName("com.epam.lab.model.sweets." + name);
                items.add(((Sweets) cl.getConstructor(double.class, double.class).newInstance(sugar, weight)));
            }
        } catch (Exception e) {
            LOG.error("exception with parsing xml file", e);
            throw new XmlParseException("exception with parsing xml file", e);
        }

        return items;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Sweets> parse(File file) throws XmlParseException {

        ArrayList<Sweets> items = new ArrayList<Sweets>();
        try {
            Document doc;
            try {
                doc = builder.parse(file);
            } catch (Exception ex) {
                LOG.error("xml file not found", ex);
                throw new XmlNotFoundException("xml file not found", ex);
            }

            int itemCount = Integer.parseInt(path.evaluate("count(/gift/item)", doc));

            for (int i = 1; i <= itemCount; i++) {

                double sugar;
                sugar = Double.parseDouble(path.evaluate("/gift/item[" + i + "]/@sugar", doc));
                String name = path.evaluate("/gift/item[" + i + "]/name", doc);
                double weight = Double.parseDouble(path.evaluate("/gift/item[" + i
                        + "]/weight", doc));
               @SuppressWarnings("rawtypes")
                Class cl = Class.forName("com.epam.lab.model.sweets." + name);
                items.add(((Sweets) cl.getConstructor(double.class, double.class)
                        .newInstance(sugar, weight)));

            }
        } catch (Exception e) {
            LOG.error("exception with parsing xml file", e);
            throw new XmlParseException("exception parsing xml file", e);
        }

        return items;
    }

    // easy test

}
