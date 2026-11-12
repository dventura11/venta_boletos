load("@rules_java//java:defs.bzl", "java_binary", "java_library")

# Compilar todas las clases de Java del paquete com.tenochsoftware
java_library(
    name = "ticketera_lib",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
    deps = [
        "@maven//:org_springframework_boot_spring_boot_starter_web",
        "@maven//:org_springframework_boot_spring_boot_starter_thymeleaf",
        "@maven//:org_springframework_boot_spring_boot_starter_data_jpa",
        "@maven//:com_h2database_h2",
        "@maven//:jakarta_persistence_jakarta_persistence_api",
        "@maven//:org_springframework_boot_spring_boot",
        "@maven//:org_springframework_boot_spring_boot_autoconfigure",
        "@maven//:org_springframework_data_spring_data_commons",
        "@maven//:org_springframework_data_spring_data_jpa",
        "@maven//:org_springframework_spring_context",
        "@maven//:org_springframework_spring_web",
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
    ],
)

# Ejecutable oficial nativo de Bazel 9
java_binary(
    name = "ticketera_app",
    main_class = "com.tenochsoftware.TicketeraApplication",
    runtime_deps = [":ticketera_lib"],
)
