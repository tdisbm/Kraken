package environment.component.util.url;


import java.util.*;
import java.util.stream.Collectors;

public class UrlBuilder {
    private Map<String, String> parameters = new HashMap<>();

    private String host;

    final public static int BUILD_QUERY = 1;

    final public static int BUILD_URL = 2;

    public UrlBuilder(String host, String port) {
        if (host == null) {
            return;
        }

        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }

        if (port != null) {
            host += ":" + port + "/";
        }

        this.host = host;
    }

    final public UrlBuilder addParameter(String key, String value) {
        if (value == null) {
            return this;
        }

        if (value.isEmpty()) {
            return this;
        }

        this.parameters.put(key, value);

        return this;
    }

    final public String getHost() {
        return this.host;
    }

    final public String buildUrl(int option) {
        Collection<String> parameters = new ArrayList<>();
        String url = "";

        switch (option) {
            case BUILD_QUERY:
                break;
            case BUILD_URL:
                url = this.host + "?";
                break;
            default:
                break;
        }

        parameters.addAll(this.parameters.entrySet().stream()
            .map(parameter -> parameter.getKey() + "=" + parameter.getValue())
            .collect(Collectors.toList())
        );

        return url + join(parameters, "&");
    }

    private static String join(Collection var0, String var1) {
        StringBuilder var2 = new StringBuilder();

        for (Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String)var3.next())) {
            if(var2.length() != 0) {
                var2.append(var1);
            }
        }

        return var2.toString();
    }
}
