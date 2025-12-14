package com.futesat.hexagonal;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.futesat.hexagonal")
public class HexagonalArchitectureTest {

        @ArchTest
        public static final ArchRule domain_should_not_depend_on_application_or_infrastructure = noClasses()
                        .that().resideInAPackage("..domain..")
                        .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..");

        @ArchTest
        public static final ArchRule application_should_not_depend_on_infrastructure = noClasses()
                        .that().resideInAPackage("..application..")
                        .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

        @ArchTest
        public static final ArchRule controllers_should_be_named_correctly = classes()
                        .that().resideInAPackage("..infrastructure.api..")
                        .and().areTopLevelClasses()
                        .and().doNotHaveSimpleName("GlobalExceptionHandler")
                        .and().haveSimpleNameNotEndingWith("Test")
                        .should().haveSimpleNameEndingWith("Controller");

        // Extra rule: Value Objects and Entities must be in domain
        // Regla extra: los Value Objects y Entidades deben estar en dominio
        @ArchTest
        public static final ArchRule domain_model_should_be_in_domain_package = classes()
                        .that().haveSimpleNameEndingWith("Id").or().haveSimpleNameEndingWith("Name")
                        .should().resideInAPackage("..domain..");
}
