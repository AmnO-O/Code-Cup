---
name: Artisan Brew System
colors:
  surface: '#FFFFFF'
  surface-dim: '#ddd9d5'
  surface-bright: '#fdf9f4'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f7f3ee'
  surface-container: '#f1ede8'
  surface-container-high: '#ebe8e3'
  surface-container-highest: '#e5e2dd'
  on-surface: '#2B211B'
  on-surface-variant: '#6E5B4E'
  inverse-surface: '#31302d'
  inverse-on-surface: '#f4f0eb'
  outline: '#E4D6C9'
  outline-variant: '#d4c3bc'
  surface-tint: '#7a5646'
  primary: '#31170b'
  on-primary: '#ffffff'
  primary-container: '#F3E4D7'
  on-primary-container: '#be927f'
  inverse-primary: '#ecbca8'
  secondary: '#a53c1b'
  on-secondary: '#ffffff'
  secondary-container: '#fe7e57'
  on-secondary-container: '#6f1a00'
  tertiary: '#022400'
  on-tertiary: '#ffffff'
  tertiary-container: '#143b0b'
  on-tertiary-container: '#7aa76b'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdbcc'
  primary-fixed-dim: '#ecbca8'
  on-primary-fixed: '#2e1508'
  on-primary-fixed-variant: '#603f30'
  secondary-fixed: '#ffdbd1'
  secondary-fixed-dim: '#ffb5a0'
  on-secondary-fixed: '#3b0a00'
  on-secondary-fixed-variant: '#842504'
  tertiary-fixed: '#c0f0ad'
  tertiary-fixed-dim: '#a4d393'
  on-tertiary-fixed: '#022100'
  on-tertiary-fixed-variant: '#28501e'
  background: '#fdf9f4'
  on-background: '#1c1c19'
  surface-variant: '#e5e2dd'
  accent-container: '#FBE3DA'
typography:
  display-hero:
    fontFamily: Poppins
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 34px
  headline-lg:
    fontFamily: Poppins
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Poppins
    fontSize: 17px
    fontWeight: '500'
    lineHeight: 22px
  body-md:
    fontFamily: Inter
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '500'
    lineHeight: 16px
  caption:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  price-display:
    fontFamily: Poppins
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 22px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 8px
  sm: 12px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 12px
  margin: 16px
---

## Brand & Style
The design system is crafted to evoke the sensory experience of a high-end specialty coffee shop. The brand personality is **warm, tactile, and professional**, balancing the heritage of coffee craftsmanship with modern digital convenience.

The visual direction follows a **Modern Artisan** approach:
- **Warmth & Comfort:** Utilizing a "paper and cream" base rather than sterile white to create an inviting atmosphere.
- **Tactile Quality:** Using generous corner radii and color-blocking to make elements feel like physical objects (stamps, menus, coasters).
- **Professional Precision:** High-contrast typography and a strict 4dp grid ensure the app feels reliable and high-performance.
- **Minimalist Depth:** Relying on tonal shifts and subtle outlines rather than heavy shadows to maintain a clean, contemporary aesthetic.

## Colors
The palette is deeply rooted in the coffee-making process. The **Primary** (Espresso Brown) provides grounding and authority, used for headers and primary messaging. The **Secondary** (Terracotta) serves as an energetic accent for interactive elements, mimicking the warmth of roasted beans.

**Background & Surface**
The `neutral` color (#FFFBF6) serves as the "paper" background for all screens. `surface` (#FFFFFF) is reserved for cards and sheets to provide a lift without needing heavy shadows.

**Functional Colors**
- **Success:** A deep botanical green used for loyalty rewards and order completions.
- **Outline:** A soft café-au-lait tone used for structural dividers and unselected states.
- **On-Surface-Variant:** A muted bean-tone used for secondary metadata and helper text.

## Typography
The system uses a sophisticated pairing of **Poppins** and **Inter**. 

**Poppins** is used for all branding, headings, and price points. Its geometric yet friendly curves reinforce the "artisan" feel. A specific `price-display` role is defined to ensure monetary values are scannable and prominent.

**Inter** handles all functional text. Its high legibility and neutral character provide a professional counterpoint to the more expressive Poppins headings. 

On mobile devices, headings scale down to `headline-lg` to ensure no more than three words span a single line. All line heights follow a tight 4px baseline alignment.

## Layout & Spacing
The layout follows a **4dp base unit** rhythm, creating a predictable and harmonious flow. 

**Grid Strategy:**
- **Mobile:** A fluid 2-column grid for product listings with a 12px gutter.
- **Margins:** A standard 16px safe area is maintained on all screen edges.
- **Sectioning:** Sections are separated by 24px (lg) of vertical space, while items within a group are separated by 12px (sm).

**Interactive Targets:**
All buttons and touchpoints must maintain a minimum hit area of 48x48px, even if the visual element (like a chip) is smaller.

## Elevation & Depth
This design system prioritizes **Tonal Layers** over shadows to maintain a clean, modern aesthetic. 

- **Level 0 (Background):** The warm #FFFBF6 base.
- **Level 1 (Cards/Sheets):** Pure white surfaces (#FFFFFF) that sit on the background. Separation is achieved via the `outline` border or a very subtle shift in background color rather than a shadow.
- **Level 2 (Overlays):** Bottom sheets and floating navigation bars use a 3dp ambient shadow—a soft, low-opacity brown-tinted blur—to indicate they are temporary layers above the main content.

Scrum elements (the background when a bottom sheet is open) should use a 40% opacity tint of the Primary color to maintain brand warmth during modal states.

## Shapes
The shape language is **Rounded and Organic**, echoing the curves of a ceramic mug.

- **Primary Cards & Sheets:** Use a consistent 12px (`rounded-lg`) radius.
- **Product Images:** Use a more pronounced 16px radius to make the photography feel like a featured element.
- **Interactive Elements:** Primary buttons and chips use **Pill shapes** (999px) to maximize the "friendly" and "tactile" quality of the UI.
- **Selection States:** When an item is selected, the shape remains consistent, but the stroke weight increases or the background shifts to a container color.

## Components

**Buttons**
- **Primary CTA:** Pill-shaped, #C1502E (Terracotta) background with white text.
- **Secondary/Tonal:** 8px rounded corners, #F3E4D7 (Primary Container) background with #4A2C1E text.

**Input Fields**
- Outlined style using the `outline` token (#E4D6C9). 
- 8px corner radius. 
- Labels use `label-md` in the `on-surface-variant` color.

**Chips**
- Pill-shaped. 
- **Selected:** #F3E4D7 background with a 1px #4A2C1E border.
- **Unselected:** Transparent background with a 1px #E4D6C9 border.

**Loyalty Stamps**
- Circular elements. 
- **Earned:** #4F7942 (Success) background with a checkmark or brand icon.
- **Empty:** #E4D6C9 dashed border.

**Cards**
- White surface, 12px radius.
- No shadow; use a 1px `outline` border to define the container against the #FFFBF6 background.

**Navigation**
- Bottom Navigation Bar uses a white surface with a subtle 3dp shadow. 
- Active icons use the Primary color; inactive use On-Surface-Variant.