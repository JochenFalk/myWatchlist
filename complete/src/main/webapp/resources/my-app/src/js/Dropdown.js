let newOptions = [];
let newQueryResults = [];
let isUpdated = false;

function resetFields() {

    // let dropDownContainers = document.getElementsByClassName('dropDownContainer');
    //
    // for (let i = 0; i < dropDownContainers.length; i++) {
    //     let parentId = dropDownContainers[i].parentElement.getAttribute('id')
    //     if (parentId !== "dropdown1") {
    //         let resultContainer = dropDownContainers[i].nextSibling;
    //         let dropdownOption = dropDownContainers[i].firstChild.firstChild.nextSibling.nextSibling.firstChild;
    //         // $(resultContainer).fadeOut(FADEOUT_TIME);
    //         $(dropdownOption).trigger('click');
    //         setTimeout(function () {
    //             $(dropdownOption).trigger('click');
    //             if (dropDownContainers[i].classList.contains('open')) {
    //                 $(dropDownContainers[i]).trigger('click');
    //             }
    //         }, 10);
    //     }
    // }
}

function updateFields() {

    // let dropDownContainers = document.getElementsByClassName('dropDownContainer');
    //
    // for (let i = 0; i < dropDownContainers.length; i++) {
    //     let resultContainer = dropDownContainers[i].nextSibling;
    //     let dropdownValue = dropDownContainers[i].firstChild.firstChild.nextSibling.firstChild;
    //
    //         if (dropdownValue.innerText === "Select an option") {
    //             $(resultContainer).fadeOut(FADEOUT_TIME);
    //         } else {
    //             $(resultContainer).fadeIn(FADEOUT_TIME);
    //         }
    // }
}

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
            setMainState: function (mainState) {
                this.resetOptions(mainState);
            },
            selectionCount: 1,
            increaseSelectionCount: function (id) {
                this.setCount(id, 1);
            },
            decreaseSelectionCount: function (id) {
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
        // this.dropdown4.resetOptions();
        // if (this.dropdown4.state.addClass === true) {
        //     this.dropdown4.toggleDropDown();
        // }
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
            case "4":
                if (this.dropdown4.state.dropDownValue !== "Select an option" && modifier === -1) {
                    this.updateCount(modifier);
                }
                if (this.dropdown4.state.dropDownValue === "Select an option" && modifier === 1) {
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
                          ref={dropdown1 => {
                              this.dropdown1 = dropdown1;
                          }}/>
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
                            },
                            (error) => {
                            });
                } else {
                    let emptyArray = []
                    this.field.displaySelection(emptyArray);
                    this.setState({
                        options: ["Users", "Lists", "Search results", "Messages"]
                    })
                }
            } else {
                console.log("You are logged out!")
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
                {/*<input id="listFormSubmit" className="button" value="Load list"/>*/}
                {/*<input id="listFormDelete" className="button" value="Delete list"/>*/}
                {/*<input id="listFormCreate" className="button" value="Create list"/>*/}
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
                if (i === 0) {
                    updateFields();
                }
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
                                     onClick={evt => this.openAdminBox(this.props, index)}>{titles}
                        </span>
                    })
                }
            </div>
        )
    }
}

// class Input extends React.Component {
//     constructor(props) {
//         super(props);
//
//         this.state = {
//             values: newOptions
//         }
//     }
//
//     render() {
// console.log("force render")
//         return (
//             <React.Fragment>
//                 {
//                     this.state.values.map((values, index) => {
//                         return <input key={index} placeholder="input" type="text"> {values} </input>
//                     })
//                 }
//             </React.Fragment>
//         )
//     }
// }

ReactDOM.render(
    <React.Fragment>
        <App/>
    </React.Fragment>,
    document.getElementById('root')
);

// ReactDOM.render(
//     <React.Fragment>
//         <Input/>
//     </React.Fragment>,
//     document.getElementById('adminForm')
// );
