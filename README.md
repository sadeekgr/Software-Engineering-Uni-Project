# Codex Naturalis

<p align="center">
    <img src="https://github.com/Lv1g1/ing-sw-2024-Inguaggiato-Manzoni-Mancini-Karagoda_Gamage_Ranasinghe/assets/70089781/c369f6c2-cb1e-4509-b801-3d52ee75e64c" alt="Image Description" width="600">
</p>

**Final project of the "Software Engineering" course of "Computer Science and Engineering" - Politecnico di Milano (23/24).**

## The Team
- Filippo Mancini
- Carlo Manzoni
- Luigi Inguaggiato
- Sadeesha Karagoda Gamage Ranasinghe

## Deliverables
- [Class Diagram](CodexNaturalis/deliverables/final/uml/ClassDiagram.pdf)
- [Class Diagram Model](CodexNaturalis/deliverables/final/uml/ClassDiagramModel.pdf)
- [Sequence Diagram Join Match](CodexNaturalis/deliverables/final/uml/SequenceDiagramJoinMatch.pdf)
- [Sequence Diagram Play Card](CodexNaturalis/deliverables/final/uml/SequenceDiagramPlayCard.pdf)
- [Sequence Diagram Draw Card](CodexNaturalis/deliverables/final/uml/SequenceDiagramDrawCard.pdf)

## Specifications
The project consists of a Java version of the board game Codex Naturalis, made by Cranio Creations.

The full game can be found [here](https://www.craniocreations.it/prodotto/codex-naturalis).

The final version includes:
- Final UML diagrams
- Working game implementation
- Network protocol documentation of the application
- Source code of the implementation
- Source code of the unit tests

## Implemented Functionalities

| Functionality  | Status |
|----------------|--------|
| Basic rules    | ✅     |
| Complete rules | ✅     |
| Socket         | ✅     |
| RMI            | ✅     |
| CLI            | ✅     |
| GUI            | ✅     |

## Advanced Functionality

| Functionality        | Status |
|----------------------|--------|
| Chat                 | ✅    |
| Persistence          | ✅    |
| Multiple games       | ✅    |


### Test cases
The model package's class coverage is 100%.

| Package   | Tested Class  | Coverage      |
|-----------|---------------|---------------|
| Model     | Global Package| 93% (729/781) |

Coverage criteria: code lines.

## Javadocs
This project contains numerous methods and classes, which can sometimes make understanding their purposes challenging. To address this, each method and class is documented with detailed comments.

[Documentation](CodexNaturalis/deliverables/javadoc)

## How to run the game
We have chosen to organize our application into two separate JAR files for the server and client components. You can find both files inside the [Deliverables](CodexNaturalis/deliverables/final/jar) folder. To run the application:

+ Open a terminal on your computer.
+ Navigate to the directory where the JAR files are located.
  
To start the server, use the following command:

Server
```bash
java -jar server.jar -ip:your_server_ip
```

Client (with GUI)
```bash
java -jar client.jar
```

Client (with CLI)
```bash
java -jar client.jar -cli
```

If you need to run multiple client instances, repeat the client command as needed.

## Game Screenshot
GUI
<p align="left">
    <img src=CodexNaturalis/screenshots/gui.png alt="Image Description" width="600">
</p>
CLI
<p align="left">
    <img src=CodexNaturalis/screenshots/cli.png alt="Image Description" width="600">
</p>

## Software used
- **IntelliJ IDEA Ultimate:** main IDE
- **GitHub Desktop:** github
- **Scenebuilder:** GUI
