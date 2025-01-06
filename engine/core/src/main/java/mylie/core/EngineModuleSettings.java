package mylie.core;

import mylie.core.component.Component;

/// A generic interface for defining settings of an engine module and constructing
/// configured instances of that module.
///
/// @param <T> the type of module that the settings are associated with
public interface EngineModuleSettings<T extends Component> {
    /// Builds and returns an instance of the module configured by this settings instance.
    ///
    /// @return an instance of the module of type `T`, created based on the configuration
    ///         defined within this settings instance.
    T build();
}
