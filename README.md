# ElytraRace

<div align="center">

![ElytraRace Banner](https://via.placeholder.com/800x200/4A90E2/FFFFFF?text=ElytraRace)

**A competitive elytra racing plugin for Minecraft Paper servers**

[![Build Status](https://img.shields.io/github/actions/workflow/status/NindroidA/ElytraRace/build.yml?branch=main)](https://github.com/NindroidA/ElytraRace/actions)
[![License](https://img.shields.io/github/license/NindroidA/ElytraRace)](LICENSE)
[![Version](https://img.shields.io/github/v/release/NindroidA/ElytraRace)](https://github.com/NindroidA/ElytraRace/releases)
[![Issues](https://img.shields.io/github/issues/NindroidA/ElytraRace)](https://github.com/NindroidA/ElytraRace/issues)

[Features](#-features) • [Installation](#-quick-start) • [Commands](#-commands) • [Documentation](#-documentation) • [Contributing](#-contributing)

</div>

---

## 📖 Overview

ElytraRace transforms your Minecraft server into a competitive racing arena. Players navigate custom-designed courses, flying through sequential ring checkpoints while racing against the clock and each other. With built-in anti-cheat, comprehensive statistics tracking, and flexible course design tools, ElytraRace provides a complete racing experience.

### What Makes It Special

- **Advanced Ring System**: POINT and REGION ring types with orientation support
- **In-Race Navigation**: GTA-style direction arrows and ring particles guide racers
- **Sound Effects**: Audio feedback for every race event
- **Intelligent Anti-Cheat**: Rocket limits, boundary checks, and order validation
- **Performance Optimized**: Cached calculations, minimal server impact
- **WorldEdit Integration**: Select built ring structures or import from WorldGuard
- **Comprehensive Stats**: Personal bests, win rates, global leaderboards

---

## ✨ Features

### Core Racing System
- **Automatic Lobby Management** - Players join by entering start region
- **Ready-Check System** - Synchronized countdown with visual effects
- **Sequential Ring Navigation** - Must pass through checkpoints in order
- **Real-Time Timer** - Per-player and global race timing
- **Anti-Cheat Protection** - Prevents skipping, rocket abuse, and exploits

### NEW in v1.4.x — Ring System Overhaul
- **Dual Ring Types** - POINT (sphere detection) and REGION (WorldEdit cuboid selection)
- **Ring Orientations** - VERTICAL_NS, VERTICAL_EW, HORIZONTAL with orientation-aware particles
- **Ring Order Enforcement** - Players must pass rings in sequence with wrong-ring feedback
- **Sound Effects** - XP orb dings, countdown ticks, finish fanfare, DQ sounds, and more
- **In-Race Ring Particles** - Each racer sees END_ROD particles at their next ring
- **GTA-Style Navigation** - Action bar shows direction arrow and distance to next ring
- **Ring Management** - setorder, setorientation, setradius, clearrings commands
- **Mid-Race Lockout** - Players can't enter lobby during active races
- **Finish Line Celebration** - Firework + totem particle burst on completion

### Features (v1.1.0+)
- Force-Join System, Region Import, Starting Platform
- Test Mode, Personal Best Tracking, Auto-Spectator
- Ring Preview, Boundary System, Auto-Finish Timer
- Rocket Requirements, Ring Setup Commands

### Statistics & Leaderboards
- **Personal Stats**: Wins, total races, best/average times, win rates
- **Global Leaderboards**: Top 10 rankings by multiple metrics
- **Personal Bests**: Individual fastest times with achievement dates
- **Persistent Storage**: All data saved between server restarts

### Anti-Cheat Features
- ✅ Ring skip detection with instant disqualification
- ✅ Rocket usage limits (configurable, default 3 per race)
- ✅ Backward navigation prevention
- ✅ Off-course boundary warnings and teleportation
- ✅ Disconnect handling (automatic DNF status)

---

## 📋 Requirements

| Requirement | Version | Status |
|------------|---------|--------|
| **Server** | Paper 1.21.4+ | Required |
| **Java** | Java 21+ | Required |
| **WorldEdit** | 7.3.3+ | Required |
| **WorldGuard** | 7.0.13+ | Optional* |

\* *Required for region import feature*

---

## 🚀 Quick Start

### Installation

1. **Download** the latest release
   ```bash
   # From GitHub Releases
   wget https://github.com/NindroidA/ElytraRace/releases/latest/download/ElytraRace.jar
   ```

2. **Install** the plugin
   ```bash
   # Place in your server's plugins folder
   mv ElytraRace.jar /path/to/server/plugins/
   ```

3. **Start** your server
   ```bash
   # The plugin will generate default configuration
   java -Xmx4G -jar paper.jar
   ```

4. **Configure** your first race
   ```bash
   # In-game as admin
   /er setup lobby              # Set lobby spawn
   /er setup start              # Define start region (WorldEdit)
   /er setup finish             # Define finish region (WorldEdit)
   /er import rings             # Import rings from WorldGuard
   # OR manually add rings
   /er setup addring ring1 VERTICAL_NS   # POINT ring at your location
   # OR select ring with //wand, then:
   /er setup addring ring1               # REGION ring from selection
   ```

### Quick Setup Example

```bash
# Complete setup in 5 commands
/er setup lobby
/er setup start       # After making WorldEdit selection
/er setup finish      # After making WorldEdit selection
/er import rings      # Auto-imports ring1, ring2, etc.
/er preview          # Visualize the course
```

See the [Installation Guide](docs/INSTALLATION.md) for detailed instructions.

---

## 💻 Commands

### Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/er rules` | Display race rules | `race.use` |
| `/er join` | Join race lobby | `race.use` |
| `/ready` | Toggle ready status | `race.use` |
| `/er stats [player]` | View statistics | `race.stats` |
| `/er pb [player]` | View personal best | `race.use` |
| `/er top` | View leaderboard | `race.use` |
| `/er progress` | Check ring progress | `race.use` |
| `/er timer` | View race time | `race.use` |

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/er forcejoin <player>` | Force player into race | `race.admin` |
| `/er testmode` | Toggle test mode | `race.admin` |
| `/er import rings` | Import WorldGuard regions | `race.admin` |
| `/er preview` | Toggle ring preview | `race.admin` |
| `/er platform <create\|remove>` | Manage platforms | `race.admin` |
| `/er setup lobby` | Set lobby location | `race.admin` |
| `/er setup start` | Define start region | `race.admin` |
| `/er setup finish` | Define finish region | `race.admin` |
| `/er setup addring <name> [orientation]` | Add ring (POINT or REGION with WE selection) | `race.admin` |
| `/er setup removering <name>` | Remove a configured ring | `race.admin` |
| `/er setup setorder <ring> <#>` | Change ring order | `race.admin` |
| `/er setup setorientation <ring> <dir>` | Change ring orientation | `race.admin` |
| `/er setup setradius <ring> <radius>` | Change ring radius | `race.admin` |
| `/er clearrings` | Remove all rings | `race.admin` |
| `/er start` | Force start race | `race.admin` |
| `/er reset` | Reset active race | `race.admin` |

Full command reference: [COMMANDS.md](docs/COMMANDS.md)

---

## ⚙️ Configuration

### Basic Configuration

```yaml
race:
  min-players: 2
  max-players: 5
  required-rockets: 64
  max-rocket-uses: 3
  auto-finish-time: 180

rings:
  default-orientation: "VERTICAL_NS"   # VERTICAL_NS, VERTICAL_EW, HORIZONTAL
  enforce-order: true                   # Must pass rings in sequence
  default-radius: 5.0                   # Detection radius for POINT rings

region-import:
  enabled: true
  prefix: "ring"

anti-cheat:
  boundary-distance: 50
  teleport-on-exceed: true
  warnings-before-teleport: 3

spectator:
  auto-enable: true
  return-to-lobby: true
  delay-seconds: 3
```

See [Configuration Guide](docs/CONFIGURATION.md) for all options.

---

## 📚 Documentation

- **[Installation Guide](docs/INSTALLATION.md)** - Complete setup instructions
- **[Commands Reference](docs/COMMANDS.md)** - Detailed command documentation
- **[Configuration Guide](docs/CONFIGURATION.md)** - All configuration options
- **[WorldEdit Integration](docs/WORLDEDIT.md)** - Region setup guide
- **[API Documentation](docs/API.md)** - For developers
- **[Changelog](docs/CHANGELOG.md)** - Version history

---

## 🎮 How It Works

```
┌─────────────┐
│   Player    │
│  Enters     │──▶ Joins Lobby
│  Region     │
└─────────────┘
       │
       ▼
┌─────────────┐
│   Types     │
│  /ready     │──▶ Ready Status
└─────────────┘
       │
       ▼
┌─────────────┐
│ All Ready?  │──▶ Countdown: 3, 2, 1, READY, GO!
└─────────────┘
       │
       ▼
┌─────────────┐
│   Race      │──▶ Fly through rings in order
│   Starts    │    Timer + navigation arrows + ring particles
└─────────────┘
       │
       ▼
┌─────────────┐
│  Complete   │──▶ Enter finish region
│  All Rings  │    Time recorded + celebration particles
└─────────────┘
       │
       ▼
┌─────────────┐
│  Results &  │──▶ Stats updated
│ Leaderboard │    Personal best checked
└─────────────┘
```

---

## 🏗️ Building from Source

```bash
# Clone the repository
git clone https://github.com/NindroidA/ElytraRace.git
cd ElytraRace

# Build with Maven
mvn clean package

# Output: target/ElytraRace-1.4.5.jar
```

### Requirements for Building
- Maven 3.8+
- Java 21 JDK
- Git

---

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md).

### Quick Contribution Guide

1. **Fork** the repository
2. **Create** a branch: `feature/123-your-feature`
3. **Commit** your changes with clear messages
4. **Test** thoroughly on Paper 1.21.4+
5. **Submit** a pull request to `develop` branch

### Branch Naming Convention

```
feature/   - New features
bugfix/    - Bug fixes
hotfix/    - Critical fixes
docs/      - Documentation
refactor/  - Code improvements
test/      - Test additions
```

---

## 🐛 Bug Reports & Feature Requests

- **Bug Reports**: [Open an Issue](https://github.com/NindroidA/ElytraRace/issues/new?template=bug_report.md)
- **Feature Requests**: [Start a Discussion](https://github.com/NindroidA/ElytraRace/discussions/new?category=ideas)
- **Questions**: [GitHub Discussions](https://github.com/NindroidA/ElytraRace/discussions)

---

## 🔒 Security

Found a security vulnerability? Please [open an issue](https://github.com/NindroidA/ElytraRace/issues) with the `security` label.

---

## 📊 Statistics

<div align="center">

![GitHub stars](https://img.shields.io/github/stars/NindroidA/ElytraRace?style=social)
![GitHub forks](https://img.shields.io/github/forks/NindroidA/ElytraRace?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/NindroidA/ElytraRace?style=social)

</div>

---

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

### What This Means
- ✅ Commercial use allowed
- ✅ Modification allowed
- ✅ Distribution allowed
- ✅ Private use allowed
- ⚠️ Liability and warranty not provided

---

## 🙏 Acknowledgments

### Contributors
- **Kartik Fulara** - *Original Creator* - [@Kartik-Fulara](https://github.com/Kartik-Fulara)
- **NindroidA** - *Maintainer, Ring System Overhaul & Bug Fixes* - [@NindroidA](https://github.com/NindroidA)

### Dependencies
- [Paper](https://papermc.io/) - High-performance Minecraft server
- [WorldEdit](https://enginehub.org/worldedit) - Region editing
- [WorldGuard](https://enginehub.org/worldguard) - Region protection

### Special Thanks
- Paper development team
- WorldEdit/WorldGuard contributors
- Community beta testers
- All contributors and supporters

---

## 📞 Support & Community

- 🐛 **Issues**: [Report bugs](https://github.com/NindroidA/ElytraRace/issues)
- 💡 **Discussions**: [Feature requests](https://github.com/NindroidA/ElytraRace/discussions)

---

## 🗺️ Roadmap

### Completed (v1.4.x)
- [x] Ring orientation system (VERTICAL_NS, VERTICAL_EW, HORIZONTAL)
- [x] REGION ring type (WorldEdit cuboid selection)
- [x] Sound effects for all race events
- [x] In-race ring particles
- [x] GTA-style navigation indicator
- [x] Ring management commands

### Future Plans
- [ ] Team racing mode
- [ ] Economy integration (Vault)
- [ ] Multiple race tracks
- [ ] PlaceholderAPI support
- [ ] MySQL database support
- [ ] Sound effect customization via config

---

<div align="center">

**Built with ❤️ for the Minecraft community**

[⬆ Back to Top](#elytrarace)

</div>