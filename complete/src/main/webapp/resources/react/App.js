let newOptions = [];
let newQueryResults = [];
let isUpdated = false;

function buildOptionList(data) {

    newOptions = [];
    newQueryResults = [];

    const {dropDownOptions: dropDownOptions, queryResults: queryResults} = data;

    for (let i = 0; i < dropDownOptions.length; i++) {
        if (queryResults[dropDownOptions[i]] !== undefined) {
            if (queryResults[dropDownOptions[i]].length !== 0) {
                newOptions.push(dropDownOptions[i]);
            }
        }
    }

    newQueryResults = queryResults;
}

function clamp(val, min, max) {
    return val < min ? min : (val > max ? max : val);
}

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            mainState: false,
            mainDropDownValue: "Select an option",
            setMainState: (mainState) => {
                this.resetOptions(mainState);
            },
            selectionCount: 1,
            increaseSelectionCount: (id) => {
                this.setCount(id, 1);
            },
            decreaseSelectionCount: (id) => {
                this.setCount(id, -1);
            }
        }
    }

    resetOptions(mainState) {
        this.setState({
            mainState: mainState,
            selectionCount: 1
        })
        this.dropdown2.resetOptions();
        if (this.dropdown2.state.addClass === true) {
            this.dropdown2.toggleDropDown();
        }
        this.dropdown3.resetOptions();
        if (this.dropdown3.state.addClass === true) {
            this.dropdown3.toggleDropDown();
        }
    }

    setCount(id, modifier) {

        switch (id) {
            case "2":
                if (this.dropdown2.state.dropDownValue !== "Select an option" && modifier === -1) {
                    this.updateCount(modifier);
                }
                if (this.dropdown2.state.dropDownValue === "Select an option" && modifier === 1) {
                    this.updateCount(modifier);
                }
                break;
            case "3":
                if (this.dropdown3.state.dropDownValue !== "Select an option" && modifier === -1) {
                    this.updateCount(modifier);
                }
                if (this.dropdown3.state.dropDownValue === "Select an option" && modifier === 1) {
                    this.updateCount(modifier);
                }
                break;
        }
    }

    updateCount(modifier) {

        let newCount = clamp(
            this.state.selectionCount + parseInt(modifier),
            1,
            this.state.selectionCount + parseInt(modifier)
        );

        this.setState({
            selectionCount: newCount
        })
    }

    render() {

        return (
            <React.Fragment>
                <Dropdown id="1"
                          mainState={this.state.mainState}
                          setMainState={this.state.setMainState.bind(this)}
                          selectionCount={this.state.selectionCount}
                          increaseSelectionCount={this.state.increaseSelectionCount.bind(this)}
                          decreaseSelectionCount={this.state.decreaseSelectionCount.bind(this)}
                />
                <Dropdown id="2"
                          mainState={this.state.mainState}
                          setMainState={this.state.setMainState.bind(this)}
                          selectionCount={this.state.selectionCount}
                          increaseSelectionCount={this.state.increaseSelectionCount.bind(this)}
                          decreaseSelectionCount={this.state.decreaseSelectionCount.bind(this)}
                          ref={dropdown2 => {
                              this.dropdown2 = dropdown2;
                          }}/>
                <Dropdown id="3"
                          mainState={this.state.mainState}
                          setMainState={this.state.setMainState.bind(this)}
                          selectionCount={this.state.selectionCount}
                          increaseSelectionCount={this.state.increaseSelectionCount.bind(this)}
                          decreaseSelectionCount={this.state.decreaseSelectionCount.bind(this)}
                          ref={dropdown3 => {
                              this.dropdown3 = dropdown3;
                          }}/>
            </React.Fragment>
        )
    }
}

class Dropdown extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            dropDownValue: "Select an option",
            options: ["Users", "Lists", "Search results", "Messages"],
            addClass: false,
            selected: false
        }
    }

    resetOptions() {
        this.setState({
            dropDownValue: "Select an option",
            options: []
        });
        newOptions = [];
    }

    toggleDropDown() {

        let element = ReactDOM.findDOMNode(this);
        let selection = element.firstChild.firstChild.nextSibling.nextSibling.firstChild;

        if (isUpdated === true && this.props.id !== "1") {
            $(selection).addClass('selected');
            isUpdated = false;
        }

        if (this.props.id !== "1" && this.state.addClass === false) {
            this.setState({
                options: newOptions
            })
        }
        this.setState({addClass: !this.state.addClass});
    }

    selectOption(evt) {

        let selection = evt.target;
        let options = document.getElementsByClassName('dropDownOption');
        for (let i = 0; i < options.length; i++) {
            $(options[i]).removeClass('selected');
        }
        $(selection).addClass('selected');
        this.setState({
            dropDownValue: selection.innerHTML
        });

        if (this.props.id === "1") {
            if (selection.innerText === "Select an option") {
                this.props.setMainState(false);
            } else {
                this.props.setMainState(true);
            }
            this.getPrimaryQuery(selection.innerHTML)
        } else {
            if (selection.innerText !== "Select an option") {
                this.props.increaseSelectionCount(this.props.id);
            } else {
                this.props.decreaseSelectionCount(this.props.id);
            }
            this.displayQueryResults(selection.innerHTML);
        }

        this.toggleDropDown();
    }

    getPrimaryQuery(selection) {
        getLoginStatus();
        promiseLoginStatus.then(data => {
            if (data) {
                if (selection !== "Select an option") {

                    let url = new URL("/getPrimaryQuery", document.baseURI);
                    url.searchParams.append("selection", selection);

                    fetch(url)
                        .then(response => {
                            return response.json();
                        })
                        .then((data) => {
                            const {returnValues: returnValues} = data;
                            this.field.displaySelection(returnValues);
                            buildOptionList(data);
                        });
                } else {
                    let emptyArray = []
                    this.field.displaySelection(emptyArray);
                    this.setState({
                        options: ["Users", "Lists", "Search results", "Messages"]
                    })
                }
            } else {
                alertFailure("You are logged out and will be redirected to the homepage.", longTimeOut);
                setTimeout(function () {
                    window.location.href = "/homePage";
                }, shortTimeOut);
            }
            isUpdated = true;
        });
    }

    displayQueryResults(selection) {
        if (selection !== "Select an option") {
            this.field.displaySelection(newQueryResults[selection]);
        } else {
            let emptyArray = []
            this.field.displaySelection(emptyArray);
        }
    }

    render() {

        let dropDownClass = ["dropDownContainer"];
        if (this.state.addClass) {
            dropDownClass.push('open');
        }

        let pointerStyle;
        let colorStyle;
        if (this.props.id !== "1") {
            if (this.props.mainState === true) {
                if (this.props.dropDownValue === "Select an option") {
                    pointerStyle = "none";
                    colorStyle = "dimgrey";
                } else {
                    pointerStyle = "auto"
                    colorStyle = "lightgrey";
                }
            } else {
                if (this.props.id !== "1")
                    pointerStyle = "none";
                colorStyle = "dimgrey";
            }
        }

        const style = {
            pointerEvents: pointerStyle,
            color: colorStyle,
            gridArea: "dropdown" + this.props.id
        }

        return (
            <React.Fragment>
                <div className={dropDownClass.join(' ')} style={style}
                     onClick={() => this.toggleDropDown()}>
                    <div className="dropdownSelect">
                        <div className="dropdownArrow"/>
                        <div className="dropDownInput" style={style}>
                            <span> {this.state.dropDownValue} </span>
                        </div>
                        <div className="dropDownOptions">
                    <span className="dropDownOption selected"
                          onClick={evt => this.selectOption(evt)}>Select an option</span>
                            <span>
                    {
                        this.state.options.map((options, index) => {
                            return <span className="dropDownOption" onClick={evt => this.selectOption(evt)}
                                         key={index}>{options}</span>
                        })
                    }
                    </span>
                        </div>
                    </div>
                </div>
                <Field id={this.props.id}
                       mainState={this.props.mainState}
                       dropDownValue={this.state.dropDownValue}
                       selectionCount={this.props.selectionCount}
                       ref={field => {
                           this.field = field;
                       }}/>
            </React.Fragment>
        )
    }
}

class Field extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            titles: []
        }
    }

    displaySelection(returnValues) {

        if (returnValues !== null) {
            let titles = [];
            for (let i = 0; i < returnValues.length; i++) {
                titles.push(returnValues[i]);
            }

            this.setState({
                titles: titles,
            })
        }
    }

    openAdminBox(props, index) {
        $('.adminBox').addClass('showAdminBox');
        loadedObject = props.dropDownValue;
        loadAdminBox(index);
    }

    render() {

        const maxWidth = 100;

        const selectionCount = parseInt(this.props.selectionCount);
        const elementId = clamp(parseInt(this.props.id), parseInt(this.props.id), parseInt(this.props.selectionCount));
        const newWidth = maxWidth / selectionCount;

        const baseOffset = newWidth * (elementId - 1); // equals zero with only first element visible
        const offsetSpacing = 0.7 * selectionCount; // creates spacing between elements
        const offsetAlignment = -0.1 + (1.28 * elementId - 1); // offsets all elements left to keep elements centered below dropdown boxes

        const offset = (baseOffset - offsetSpacing) + offsetAlignment;
        const fieldWidth = (newWidth * 8.25) - 20;

        let displayStyle;
        if (this.props.mainState === true) {
            if (this.props.dropDownValue === "Select an option") {
                displayStyle = "none";
            } else {
                displayStyle = "grid"
            }
        } else {
            if (this.props.id !== "1")
                displayStyle = "none";
        }

        const style1 = {
            display: displayStyle,
            width: newWidth + "%",
            right: -offset + "%"
        }

        const style2 = {
            width: fieldWidth + "px"
        }

        return (
            <div className="resultsContainer" style={style1}>
                {
                    this.state.titles.map((titles, index) => {
                        return <span id="admin" className="field"
                                     style={style2}
                                     key={index}
                                     onClick={() => this.openAdminBox(this.props, index)}>{titles}
                        </span>
                    })
                }
            </div>
        )
    }
}

ReactDOM.render(
    <React.Fragment>
        <App/>
    </React.Fragment>,
    document.getElementById('root')
);