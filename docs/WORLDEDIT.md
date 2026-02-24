# WorldEdit Integration Guide

Complete guide for using WorldEdit with ElytraRace v1.4.5

---

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Region Setup](#region-setup)
- [Ring Setup](#ring-setup)
- [Ring Types](#ring-types)
- [Ring Orientations](#ring-orientations)
- [WorldGuard Import](#worldguard-import)
- [Tips & Best Practices](#tips--best-practices)

---

## Overview

ElytraRace uses WorldEdit for defining race regions (start, finish) and for creating precise REGION-type rings. WorldEdit's selection wand (`//wand`) is used to select areas in-game, which ElytraRace then saves as race boundaries.

---

## Requirements

| Plugin | Version | Required |
|--------|---------|----------|
| **WorldEdit** | 7.3.3+ | Yes |
| **WorldGuard** | 7.0.13+ | Optional (for region import) |

---

## Region Setup

### Start Region

The start region is where players gather before a race. Players automatically join the race when they enter this area.

```bash
# 1. Get WorldEdit wand
//wand

# 2. Left-click one corner of the start area (pos1)
# 3. Right-click the opposite corner (pos2)

# 4. Save as start region
/er setup start
```

**Tips**:
- Make the start area large enough for your max player count
- Include some vertical space so players can glide off platforms
- The region should be enclosed so players don't accidentally leave

### Finish Region

The finish region detects when racers complete the course.

```bash
# 1. Select the finish area with //wand
# 2. Save as finish region
/er setup finish
```

**Tips**:
- Make the finish region wide enough that players can't miss it
- Place it after the last ring
- A tall, narrow region (like a wall) works well

---

## Ring Setup

### Method 1: POINT Rings (Stand & Add)

Stand at the center of where you want the ring and run:

```bash
/er setup addring ring1                    # Default orientation (VERTICAL_NS)
/er setup addring ring2 VERTICAL_EW        # Specify orientation
/er setup addring ring3 HORIZONTAL          # Horizontal ring
```

POINT rings use sphere detection — players must fly within the configured radius (default 5.0 blocks) of the center point.

### Method 2: REGION Rings (WorldEdit Selection)

For precise detection matching a built ring structure:

```bash
# 1. Get WorldEdit wand
//wand

# 2. Select the ring structure
#    Left-click one corner, right-click the opposite corner
#    The selection should tightly enclose your built ring

# 3. Add the ring
/er setup addring ring1                    # Default orientation
/er setup addring ring2 VERTICAL_EW        # With orientation
```

When a WorldEdit selection is active, `/er setup addring` automatically creates a **REGION** ring instead of a POINT ring. REGION rings use cuboid detection — players must fly within the selected bounding box.

---

## Ring Types

### POINT

- **Detection**: Sphere around a center point
- **Radius**: Configurable (default 5.0 blocks, change with `/er setup setradius`)
- **Best for**: Simple courses, rings in open air, quick setup
- **Created by**: Running `/er setup addring` without a WorldEdit selection

### REGION

- **Detection**: Cuboid bounding box (min/max corners)
- **Precision**: Matches exactly what you selected with WorldEdit
- **Best for**: Built ring structures, precise hit boxes, complex shapes
- **Created by**: Running `/er setup addring` with an active WorldEdit selection

---

## Ring Orientations

Orientations control how ring preview particles and in-race particles are rendered:

| Orientation | Plane | Description | Best For |
|-------------|-------|-------------|----------|
| `VERTICAL_NS` | XY | Ring faces North/South | Rings along N-S flight paths |
| `VERTICAL_EW` | ZY | Ring faces East/West | Rings along E-W flight paths |
| `HORIZONTAL` | XZ | Ring faces Up/Down | Dive-through or climb-through rings |

### Setting Orientation

```bash
# When adding a ring
/er setup addring ring1 VERTICAL_EW

# Change existing ring orientation
/er setup setorientation ring1 HORIZONTAL
```

### Default Orientation

Set in `config.yml`:

```yaml
rings:
  default-orientation: "VERTICAL_NS"    # Applied to new rings without explicit orientation
```

---

## WorldGuard Import

If you have WorldGuard installed, you can create regions named `ring1`, `ring2`, etc. and import them all at once.

### Step 1: Create WorldGuard Regions

```bash
//wand

# Select first ring area
//pos1  ->  //pos2
/rg define ring1

# Repeat for all rings
/rg define ring2
/rg define ring3
# ... etc
```

### Step 2: Import

```bash
/er import rings
```

**Output**:
```
[ElytraRace] Importing rings from WorldGuard...
[ElytraRace] Successfully imported 12 ring(s)!
[ElytraRace] Use /er listrings to view them.
```

### Configuration

```yaml
region-import:
  enabled: true       # Enable import feature
  prefix: "ring"      # Region name prefix (detects ring1, ring2, etc.)
  auto-detect: true   # Auto-detect on startup
```

---

## Ring Management Commands

After adding rings, fine-tune them with these commands:

| Command | Description |
|---------|-------------|
| `/er listrings` | List all rings with type, order, orientation, radius |
| `/er setup setorder <ring> <#>` | Change ring order number |
| `/er setup setorientation <ring> <dir>` | Change ring orientation |
| `/er setup setradius <ring> <radius>` | Change detection radius (POINT only) |
| `/er setup removering <ring>` | Remove a single ring |
| `/er clearrings` | Remove all rings |
| `/er preview` | Toggle particle preview |

---

## Tips & Best Practices

### Course Design

1. **Use REGION rings for built structures** — Select the exact ring structure for precise hit detection
2. **Use POINT rings for open air** — Simpler setup when there's no physical ring to match
3. **Match orientation to flight direction** — A ring on a N-S path should be `VERTICAL_NS`
4. **Test with preview** — `/er preview` shows orientation-aware particles so you can verify
5. **Test with testmode** — `/er testmode` lets you fly through without affecting stats

### Ring Order

- Rings auto-increment order when added (first ring = order 1, second = order 2, etc.)
- Use `/er setup setorder` to rearrange if needed
- Use `/er listrings` to see current order (rings are displayed sorted by order)
- When `rings.enforce-order: true` (default), players must pass rings sequentially

### Performance

- REGION rings are slightly more efficient than POINT rings (simple bounds check vs. distance calculation)
- In-race ring particles only render within 100 blocks of the player
- Ring detection is cached and optimized for minimal server impact

---

## Troubleshooting

### "You need a WorldEdit selection first"

**Cause**: No active WorldEdit selection when running `/er setup start`, `/er setup finish`

**Solution**:
```bash
//wand              # Get the selection wand
# Left-click pos1, right-click pos2
/er setup start     # Now it works
```

### Ring added as POINT instead of REGION

**Cause**: No active WorldEdit selection when adding ring

**Solution**: Make a WorldEdit selection first, then run `/er setup addring <name>`

### Rings not detecting players

**Solutions**:
1. Check ring radius: `/er listrings` shows radius for each ring
2. Increase radius: `/er setup setradius <ring> 8.0`
3. For REGION rings, ensure the selection fully encloses the flyable area
4. Use `/er preview` to visualize ring boundaries

---

**WorldEdit Integration Guide v1.4.5**
Last Updated: 2026-02-23
Maintained By: NindroidA