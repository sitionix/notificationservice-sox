package com.sitionix.ntfssox.application.config;

import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.VerifyChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationHandlerBeanFactoryProcessorTest {

    @Test
    void givenRegistryWithValidHandler_whenPostProcess_thenAddsConstructorArg() {
        //given
        final DefaultListableBeanFactory registry = getRegistry();
        final GenericBeanDefinition definition = getBeanDefinition(TestNotificationHandler.class);
        registry.registerBeanDefinition("email-verification", definition);

        final MockEnvironment environment = getEnvironmentWithMessageProperties();
        final NotificationHandlerBeanFactoryProcessor subject = getProcessor(environment);

        //when
        subject.postProcessBeanDefinitionRegistry(registry);

        //then
        assertThat(definition.getConstructorArgumentValues().getArgumentCount()).isEqualTo(1);
        final Object argumentValue = definition.getConstructorArgumentValues()
                .getGenericArgumentValues()
                .get(0)
                .getValue();
        assertThat(argumentValue).isInstanceOf(MessageProperties.class);
        final MessageProperties props = (MessageProperties) argumentValue;
        assertThat(props).isEqualTo(getMessageProperties());
    }

    @Test
    void givenRegistryWithValidHandlerAndMissingMessageConfig_whenPostProcess_thenThrows() {
        //given
        final DefaultListableBeanFactory registry = getRegistry();
        final GenericBeanDefinition definition = getBeanDefinition(TestNotificationHandler.class);
        registry.registerBeanDefinition("email-verification", definition);

        final MockEnvironment environment = getEnvironment();
        final NotificationHandlerBeanFactoryProcessor subject = getProcessor(environment);

        //when
        assertThatThrownBy(() -> subject.postProcessBeanDefinitionRegistry(registry))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No message config for handler: email-verification");
    }

    @Test
    void givenRegistryWithHandlerWithoutMessagePropertiesConstructor_whenPostProcess_thenThrows() {
        //given
        final DefaultListableBeanFactory registry = getRegistry();
        final GenericBeanDefinition definition = getBeanDefinition(NoMessagePropertiesConstructorHandler.class);
        registry.registerBeanDefinition("email-verification", definition);

        final NotificationHandlerBeanFactoryProcessor subject = getProcessor(getEnvironment());

        //when
        assertThatThrownBy(() -> subject.postProcessBeanDefinitionRegistry(registry))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("NotificationHandler " + NoMessagePropertiesConstructorHandler.class.getName()
                        + " must declare a constructor with MessageProperties argument");
    }

    @Test
    void givenRegistryWithUnknownTemplateBeanName_whenPostProcess_thenThrows() {
        //given
        final DefaultListableBeanFactory registry = getRegistry();
        final GenericBeanDefinition definition = getBeanDefinition(TestNotificationHandler.class);
        registry.registerBeanDefinition("unknown-template", definition);

        final NotificationHandlerBeanFactoryProcessor subject = getProcessor(getEnvironment());

        //when
        assertThatThrownBy(() -> subject.postProcessBeanDefinitionRegistry(registry))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No NotificationTemplate for handler bean: unknown-template");
    }

    @Test
    void givenRegistryWithNonHandlerAndInvalidClasses_whenPostProcess_thenSkips() {
        //given
        final DefaultListableBeanFactory registry = getRegistry();
        final GenericBeanDefinition noClassDefinition = getEmptyBeanDefinition();
        final GenericBeanDefinition invalidClassDefinition = getBeanDefinition("com.sitionix.NotExisting");
        final GenericBeanDefinition nonHandlerDefinition = getBeanDefinition(NonHandlerBean.class);

        registry.registerBeanDefinition("no-class-name", noClassDefinition);
        registry.registerBeanDefinition("invalid-class", invalidClassDefinition);
        registry.registerBeanDefinition("non-handler", nonHandlerDefinition);

        final NotificationHandlerBeanFactoryProcessor subject = getProcessor(getEnvironment());

        //when
        subject.postProcessBeanDefinitionRegistry(registry);

        //then
        assertThat(nonHandlerDefinition.getConstructorArgumentValues().getArgumentCount()).isZero();
    }

    private DefaultListableBeanFactory getRegistry() {
        return new DefaultListableBeanFactory();
    }

    private GenericBeanDefinition getBeanDefinition(final Class<?> clazz) {
        final GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(clazz);
        return definition;
    }

    private GenericBeanDefinition getBeanDefinition(final String className) {
        final GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClassName(className);
        return definition;
    }

    private GenericBeanDefinition getEmptyBeanDefinition() {
        return new GenericBeanDefinition();
    }

    private MockEnvironment getEnvironmentWithMessageProperties() {
        return new MockEnvironment()
                .withProperty("notification.messages.email-verification.allowed-channels[0]", "EMAIL");
    }

    private MockEnvironment getEnvironment() {
        return new MockEnvironment();
    }

    private NotificationHandlerBeanFactoryProcessor getProcessor(final Environment environment) {
        final NotificationHandlerBeanFactoryProcessor processor = new NotificationHandlerBeanFactoryProcessor();
        processor.setEnvironment(environment);
        return processor;
    }

    private MessageProperties getMessageProperties() {
        return new MessageProperties(List.of(VerifyChannel.EMAIL), null);
    }

    private static class TestNotificationHandler extends AbstractNotificationHandler<Object> {

        private TestNotificationHandler(final MessageProperties props) {
            super(props);
        }

        @Override
        public void send(final Notification<? extends Object> notification) {
        }
    }

    private static class NoMessagePropertiesConstructorHandler extends AbstractNotificationHandler<Object> {

        private NoMessagePropertiesConstructorHandler(final String value) {
            super(new MessageProperties());
        }

        @Override
        public void send(final Notification<? extends Object> notification) {
        }
    }

    private static class NonHandlerBean {
    }
}
