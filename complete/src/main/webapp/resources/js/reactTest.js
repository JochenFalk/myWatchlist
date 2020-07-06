// 'use strict';
const {Component} = React;
const {render} = ReactDOM;

class ReactForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            lists: null
        }
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        let url = "/getAllListsFromUser";

        fetch(url)
            .then(response => {
                return response.json();
            })
            .then((data) => {
                    this.setState({
                        lists: data
                    });
                },
                (error) => {

                });
    }

    render() {

        if (this.state.lists === null) {
            return (<div className="loginFormMessage">Fetching data</div>)
        }
        let list = this.state.lists[0];
        let items = this.state.items;
        console.log(list);
        return (
            <div>
                <form className="formWrapLoginForm" noValidate>
                    <h1>Select list item</h1>
                    <select>
                        <option> {list.listItems[0]} </option>
                    </select>
                    <label className="loginFormMessage"> {list.title} </label>
                    <label className="loginFormMessage"> {list.description} </label>
                </form>
                <br/>
                <br/>
            </div>
        )
    }
}

ReactDOM.render(<ReactForm/>, document.getElementById("ReactForm"));