declare module "react-layout-components" {

    import * as React from 'react';

    export = ReactLayoutComponents;

    namespace ReactLayoutComponents {

        interface StandardProps extends React.DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement> {
        }

        type SizeValue = number | string

        interface SizeProps {
            width?: SizeValue
            height?: SizeValue
            minWidth?: SizeValue
            maxWidth?: SizeValue
            minHeight?: SizeValue
            maxHeight?: SizeValue
        }

        interface PaddingProps {
            padding?: SizeValue
            paddingLeft?: SizeValue
            paddingTop?: SizeValue
            paddingRight?: SizeValue
            paddingBottom?: SizeValue
        }

        interface MarginProps {
            margin?: SizeValue
            marginLeft?: SizeValue
            marginTop?: SizeValue
            marginRight?: SizeValue
            marginBottom?: SizeValue
        }

        interface BorderProps {
            border?: string
            borderColor?: string
            borderWidth?: SizeValue
            borderStyle?: string
            borderTop?: string | boolean
            borderLeft?: string | boolean
            borderBottom?: string | boolean
            borderRight?: string | boolean
        }

        interface PositionProps {
            top?: SizeValue
            left?: SizeValue
            bottom?: SizeValue
            right?: SizeValue
        }

        type OverflowTypes = "visible" | "hidden" | "scroll" | "auto" | "initial" | "inherit"
        type TextOverflowTypes = "clip" | "ellipsis" | "initial" | "inherit" | string
        type WhiteSpaceTypes = "normal" | "nowrap" | "pre" | "pre-wrap" | "pre-line"

        interface FlowProps {
            overflow?: OverflowTypes
            overflowX?: OverflowTypes
            overflowY?: OverflowTypes
            textOverflow?: TextOverflowTypes
            whiteSpace?: WhiteSpaceTypes
        }

        type BoxSizingTypes = "border-box" | "content-box" | "padding-box"

        interface BoxSizingProps {
            boxSizing?: BoxSizingTypes
        }

        type FlexBasis = string
        type FlexWrap = "flex-direction" | "flex-wrap"
        type FlexAlignContent = "center" | "flex-start" | "flex-end" | "space-around" | "space-between"
        type FlexJustifyContent = FlexAlignContent
        type FlexAlignItems = "center" | "flex-start" | "flex-end" | "baseline" | "stretch"
        type FlexAlignSelf = FlexAlignItems

        interface BoxProps extends StandardProps, SizeProps {
            flex?: number | string
            flexGrow?: number
            flexShrink?: number
            flexBasis?: FlexBasis
            inline?: boolean
            column?: boolean
            reverse?: boolean
            wrap?: FlexWrap
            alignContent?: FlexAlignContent
            justifyContent?: FlexJustifyContent
            alignItems?: FlexAlignItems
            alignSelf?: FlexAlignSelf
            fit?: boolean
            center?: boolean
        }

        type Box = React.ClassicComponent<BoxProps, {}>;
        const Box: React.ClassicComponentClass<BoxProps>;

        interface ContainerProps extends StandardProps, PaddingProps, MarginProps, BorderProps, SizeProps, PositionProps, FlowProps, BoxSizingProps {
            fixed?: boolean
            absolute?: boolean
        }

        type Container = React.ClassicComponent<ContainerProps, {}>;
        const Container: React.ClassicComponentClass<ContainerProps>;

        interface PageProps extends StandardProps {
        }

        type Page = React.ClassicComponent<PageProps, {}>;
        const Page: React.ClassicComponentClass<PageProps>;

        interface ScrollViewProps extends StandardProps, BoxProps {
            height?: SizeValue
            width?: SizeValue
            horizontal?: boolean
            initialScrollPos?: number
        }

        interface ScrollViewMethods {
            getScrollPosition(): number
            scrollTo(scrollPosition: number): void
            scrollToStart(): void
            scrollToEnd(): void
        }

        type ScrollView = React.ClassicComponent<ScrollViewProps, {}> & ScrollViewMethods;
        const ScrollView: React.ClassicComponentClass<ScrollViewProps> & ScrollViewMethods;

        type VBox = React.ClassicComponent<BoxProps, {}>
        const VBox: React.ClassicComponentClass<BoxProps>;

        type Center = React.ClassicComponent<BoxProps, {}>
        const Center: React.ClassicComponentClass<BoxProps>;
    }
}