# DatasetIsBinary
An Op for ImageJ which checks if the given Dataset is binary. A Dataset is considered binary if its elements contain only one or two distinct values. One of these values is considered to represent the foreground particles, and the other the background particles. By default the greater value is considered foreground.

**NB Dataset API is unstable and likely to undergo lots of changes**

**NB The "right" way to do things is not to check pixel values, but to limit your Ops input type to e.g. B extends BooleanType**
