package com.github.developframework.kite.spring.mvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.github.developframework.kite.core.JsonProducer;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.data.DataModel;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

/**
 * 抽象的springmvc ReturnValueHandler
 *
 * @author qiuzhenhao
 */
public abstract class AbstractKiteReturnValueHandler<T> implements HandlerMethodReturnValueHandler {

    protected KiteFactory kiteFactory;

    public AbstractKiteReturnValueHandler(KiteFactory kiteFactory) {
        this.kiteFactory = kiteFactory;
    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        ServletServerHttpResponse res = new ServletServerHttpResponse(response);
        final HttpHeaders headers = res.getHeaders();
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        }
        return res;
    }

    protected JsonEncoding getJsonEncoding(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            Charset charset = contentType.getCharset();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType().isAssignableFrom(returnType.getParameterType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleReturnValue(Object returnValue, MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        Assert.isInstanceOf(returnType(), returnValue);
        T t = (T) returnValue;
        mavContainer.setRequestHandled(true);
        final HttpOutputMessage outputMessage = this.createOutputMessage(webRequest);
        final JsonProducer jsonProducer = kiteFactory.getJsonProducer();
        JsonEncoding encoding = this.getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator generator = kiteFactory.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);
        final String namespace = namespace(t, methodParameter);
        final String templateId = templateId(t, methodParameter);
        jsonProducer.outputJson(generator, dataModel(t, methodParameter), namespace, templateId, false);
    }

    /**
     * 取得返回类型
     *
     * @return
     */
    protected abstract Class<T> returnType();

    /**
     * 取得 kite template 命名空间
     *
     * @param returnValue
     * @param methodParameter
     * @return
     */
    protected abstract String namespace(T returnValue, MethodParameter methodParameter);

    /**
     * 取得 kite template id
     *
     * @param returnValue
     * @param methodParameter
     * @return
     */
    protected abstract String templateId(T returnValue, MethodParameter methodParameter);

    /**
     * 取得 dataModel
     *
     * @param returnValue
     * @param methodParameter
     * @return
     */
    protected abstract DataModel dataModel(T returnValue, MethodParameter methodParameter);
}
