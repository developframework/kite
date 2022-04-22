package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.spring.KiteResponseBodyProcessor;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public abstract class AbstractKiteReturnValueHandler<T> implements HandlerMethodReturnValueHandler {

    protected final KiteFactory kiteFactory;

    protected final KiteResponseBodyProcessor kiteResponseBodyProcessor;

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
        final String namespace = namespace(t, methodParameter);
        final String templateId = templateId(t, methodParameter);
        final TemplateType templateType = templateType(t, methodParameter);
        final DataModel dataModel = dataModel(t, methodParameter);

        final Producer producer;
        if (templateType == TemplateType.JSON) {
            producer = kiteFactory.getJsonProducer(dataModel, namespace, templateId);
        } else {
            producer = kiteFactory.getXmlProducer(dataModel, namespace, templateId);
        }
        final String payload = producer.produce(false);
        if (kiteResponseBodyProcessor != null) {
            kiteResponseBodyProcessor.beforeWrite(methodParameter, webRequest, payload);
        }
        final HttpOutputMessage outputMessage = this.createOutputMessage(webRequest, templateType);
        final Charset charset = outputMessage.getHeaders().getContentType().getCharset();
        producer.output(outputMessage.getBody(), charset, false);
    }

    private ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest, TemplateType templateType) {
        final HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        ServletServerHttpResponse res = new ServletServerHttpResponse(response);
        final HttpHeaders headers = res.getHeaders();
        if (headers.getContentType() == null) {
            headers.setContentType(
                    templateType == TemplateType.JSON ? MediaType.APPLICATION_JSON_UTF8 : MediaType.APPLICATION_XML
            );
        }
        return res;
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
