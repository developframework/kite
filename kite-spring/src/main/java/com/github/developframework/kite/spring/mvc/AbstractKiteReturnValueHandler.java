package com.github.developframework.kite.spring.mvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.github.developframework.kite.core.JsonProducer;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.XmlProducer;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.spring.KiteResponseBodyProcessor;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 抽象的springmvc ReturnValueHandler
 *
 * @author qiuzhenhao
 */
@RequiredArgsConstructor
public abstract class AbstractKiteReturnValueHandler<T> implements HandlerMethodReturnValueHandler {

    protected final KiteFactory kiteFactory;

    @Autowired(required = false)
    protected KiteResponseBodyProcessor kiteResponseBodyProcessor;

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        final HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
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
        mavContainer.setRequestHandled(true);
        final T t = (T) returnValue;
        final HttpOutputMessage outputMessage = this.createOutputMessage(webRequest);
        final String namespace = namespace(t, methodParameter);
        final String templateId = templateId(t, methodParameter);
        final TemplateType templateType = templateType(t, methodParameter);
        final DataModel dataModel = dataModel(t, methodParameter);
        switch (templateType) {
            case JSON: {
                final JsonProducer jsonProducer = kiteFactory.getJsonProducer();
                if (kiteResponseBodyProcessor == null) {
                    final JsonEncoding encoding = this.getJsonEncoding(outputMessage.getHeaders().getContentType());
                    final JsonGenerator generator = kiteFactory.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);
                    jsonProducer.outputJson(generator, dataModel, namespace, templateId, false);
                } else {
                    final OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8);
                    final String json = jsonProducer.produce(dataModel, namespace, templateId, false);
                    kiteResponseBodyProcessor.beforeWrite(methodParameter, webRequest, json);
                    writer.write(json);
                    writer.flush();
                }
            }
            break;
            case XML: {
                final OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8);
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_XML);
                final XmlProducer xmlProducer = kiteFactory.getXmlProducer();
                if (kiteResponseBodyProcessor == null) {
                    xmlProducer.outputXml(writer, dataModel, namespace, templateId, false);
                } else {
                    final String xml = xmlProducer.produce(dataModel, namespace, templateId);
                    kiteResponseBodyProcessor.beforeWrite(methodParameter, webRequest, xml);
                    writer.write(xml);
                    writer.flush();
                }
            }
            break;
        }
    }

    /**
     * 取得返回类型
     *
     * @return 返回类型
     */
    protected abstract Class<T> returnType();

    /**
     * 取得 kite template 命名空间
     *
     * @param returnValue     返回值
     * @param methodParameter controller方法参数
     * @return 命名空间
     */
    protected abstract String namespace(T returnValue, MethodParameter methodParameter);

    /**
     * 取得 kite template id
     *
     * @param returnValue     返回值
     * @param methodParameter controller方法参数
     * @return 模板ID
     */
    protected abstract String templateId(T returnValue, MethodParameter methodParameter);

    /**
     * 取得 template type
     *
     * @param returnValue     返回值
     * @param methodParameter controller方法参数
     * @return 模板类型
     */
    protected abstract TemplateType templateType(T returnValue, MethodParameter methodParameter);

    /**
     * 取得 dataModel
     *
     * @param returnValue     返回值
     * @param methodParameter controller方法参数
     * @return 数据模型
     */
    protected abstract DataModel dataModel(T returnValue, MethodParameter methodParameter);
}
