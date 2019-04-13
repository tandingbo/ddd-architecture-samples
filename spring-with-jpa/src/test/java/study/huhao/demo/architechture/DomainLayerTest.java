package study.huhao.demo.architechture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import study.huhao.demo.domain.core.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class DomainLayerTest {

    private JavaClasses classes;

    @BeforeEach
    void setUp() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("study.huhao.demo");
    }

    @Test
    void domain_layer_depend_on_rule() {
        classes()
                .that().resideInAPackage("..domain..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("java..", "..domain..")
                .as("The domain layer should only depend on the classes in the package of " +
                        "java and domain.")
                .check(classes);
    }

    @Test
    void domain_layer_implement_interface_rule() {
        classes()
                .that().resideInAPackage("..domain.models..")
                .should().implement(Entity.class)
                .orShould().implement(AggregateRoot.class)
                .orShould().implement(ValueObject.class)
                .orShould().implement(EntityId.class)
                .orShould().implement(DomainService.class)
                .orShould().beAssignableTo(Repository.class)
                .orShould().implement(DomainException.class)
                .as("The models in the domain should implement one of the markedness interfaces in " +
                        "Entity, AggregateRoot, ValueObject, EntityId, DomainService, Repository, DomainException.")
                .check(classes);
    }

    @Test
    void domain_services_should_be_named_ending_with_DomainService() {
        classes().that().resideInAPackage("..domain..")
                .and().implement(DomainService.class)
                .should().haveSimpleNameEndingWith("DomainService")
                .as("The domain services should be named ending with 'DomainService'.")
                .check(classes);
    }

    @Test
    void domain_services_can_only_be_access_by_application_services() {
        classes().that().resideInAPackage("..domain..")
                .and().implement(DomainService.class)
                .should().onlyBeAccessed().byAnyPackage("..application.services..")
                .as("The domain services can only be access by application services")
                .because("the application is the only entrance of domain.")
                .check(classes);
    }

    @Test
    void repositories_should_be_named_ending_with_Repository() {
        classes().that().resideInAPackage("..domain..")
                .and().areAssignableTo(Repository.class)
                .should().haveSimpleNameEndingWith("Repository")
                .as("The repositories should be named ending with 'Repository'.")
                .check(classes);
    }

    @Test
    void domain_exceptions_should_be_named_ending_with_Exception() {
        classes().that().resideInAPackage("..domain..")
                .and().implement(DomainException.class)
                .should().haveSimpleNameEndingWith("Exception")
                .as("The domain exceptions should be named ending with 'Exception'.")
                .check(classes);
    }
}