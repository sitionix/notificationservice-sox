package com.sitionix.ntfssox.application.config;

import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.VerifyChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.mock.env.MockEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationHandlerBeanFactoryProcessorTest {

    private NotificationHandlerBeanFactoryProcessor subject;

    @BeforeEach
    void setUp() {
        this.subject = new NotificationHandlerBeanFactoryProcessor();
    }

    @Test
    void givenRegistryWithValidHandler_whenPostProcess_thenAddsConstructorArg() {
        //given
        final DefaultListableBeanFactory registry = this.getRegistry();
        final GenericBeanDefinition definition = this.getBeanDefinition(TestNotificationHandler.class);
        registry.registerBeanDefinition("emailVerification", definition);

        final MockEnvironment environment = this.getEnvironmentWithMessageProperties();
        this.subject.setEnvironment(environment);

        //when
        this.subject.postProcessBeanDefinitionRegistry(registry);

        //then
        assertThat(definition.getConstructorArgumentValues().getArgumentCount()).isEqualTo(1);
        final Object argumentValue = definition.getConstructorArgumentValues()
                .getGenericArgumentValues()
                .get(0)
                .getValue();
        assertThat(argumentValue).isInstanceOf(MessageProperties.class);
        final MessageProperties props = (MessageProperties) argumentValue;
        assertThat(props).isEqualTo(this.getMessageProperties());
    }

    @Test
    void givenRegistryWithValidHandlerAndMissingMessageConfig_whenPostProcess_thenThrows() {
        //given
        final DefaultListableBeanFactory registry = this.getRegistry();
        final GenericBeanDefinition definition = this.getBeanDefinition(TestNotificationHandler.class);
        registry.registerBeanDefinition("emailVerification", definition);

        final MockEnvironment environment = this.getEnvironment();
        this.subject.setEnvironment(environment);

        //when
        assertThatThrownBy(() -> this.subject.postProcessBeanDefinitionRegistry(registry))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No message config for handler: emailVerification");
    }

    @Test
    void givenRegistryWithHandlerWithoutMessagePropertiesConstructor_whenPostProcess_thenThrows() {
        //given
        final DefaultListableBeanFactory registry = this.getRegistry();
        final GenericBeanDefinition definition = this.getBeanDefinition(NoMessagePropertiesConstructorHandler.class);
        registry.registerBeanDefinition("emailVerification", definition);

        this.subject.setEnvironment(this.getEnvironment());

        //when
        assertThatThrownBy(() -> this.subject.postProcessBeanDefinitionRegistry(registry))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("NotificationHandler " + NoMessagePropertiesConstructorHandler.class.getName()
                        + " must declare a constructor with MessageProperties argument");
    }

    @Test
    void givenRegistryWithUnknownTemplateBeanName_whenPostProcess_thenThrows() {
        //given
        final DefaultListableBeanFactory registry = this.getRegistry();
        final GenericBeanDefinition definition = this.getBeanDefinition(TestNotificationHandler.class);
        registry.registerBeanDefinition("unknown-template", definition);

        this.subject.setEnvironment(this.getEnvironment());

        //when
        assertThatThrownBy(() -> this.subject.postProcessBeanDefinitionRegistry(registry))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No NotificationTemplate for handler bean: unknown-template");
    }

    @Test
    void givenRegistryWithNonHandlerAndInvalidClasses_whenPostProcess_thenSkips() {
        //given
        final DefaultListableBeanFactory registry = this.getRegistry();
        final GenericBeanDefinition noClassDefinition = this.getEmptyBeanDefinition();
        final GenericBeanDefinition invalidClassDefinition = this.getBeanDefinition("com.sitionix.NotExisting");
        final GenericBeanDefinition nonHandlerDefinition = this.getBeanDefinition(NonHandlerBean.class);

        registry.registerBeanDefinition("no-class-name", noClassDefinition);
        registry.registerBeanDefinition("invalid-class", invalidClassDefinition);
        registry.registerBeanDefinition("non-handler", nonHandlerDefinition);

        this.subject.setEnvironment(this.getEnvironment());

        //when
        this.subject.postProcessBeanDefinitionRegistry(registry);

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

    private MessageProperties getMessageProperties() {
        return new MessageProperties(List.of(VerifyChannel.EMAIL), null);
    }

    private static class TestNotificationHandler extends AbstractNotificationHandler<Object> {

        TestNotificationHandler(final MessageProperties props) {
            super(props);
        }

        @Override
        public void send(final Notification<? extends Object> notification) {
            // no-op: handler behavior is not relevant for registry validation tests
        }
    }

    private static class NoMessagePropertiesConstructorHandler extends AbstractNotificationHandler<Object> {

        NoMessagePropertiesConstructorHandler() {
            super(new MessageProperties());
        }

        @Override
        public void send(final Notification<? extends Object> notification) {
            // no-op: handler behavior is not relevant for registry validation tests
        }
    }

    private static class NonHandlerBean {
    }
}
